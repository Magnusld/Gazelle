package gazelle.server.endpoint;

import gazelle.api.ChoreResponse;
import gazelle.model.Chore;
import gazelle.model.User;
import gazelle.model.UserChoreProgress;
import gazelle.server.error.ChoreNotFoundException;
import gazelle.server.repository.ChoreRepository;
import gazelle.server.repository.UserChoreProgressRepository;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class ChoreController {

    private final ChoreRepository choreRepository;
    private final UserChoreProgressRepository userChoreProgressRepository;
    private final TokenAuthService tokenAuthService;

    public ChoreController(ChoreRepository choreRepository,
                           UserChoreProgressRepository userChoreProgressRepository,
                           TokenAuthService tokenAuthService) {
        this.choreRepository = choreRepository;
        this.userChoreProgressRepository = userChoreProgressRepository;
        this.tokenAuthService = tokenAuthService;
    }

    /**
     * Make a serializable object with data about the chore and its relationships
     *
     * @param chore the chore
     * @param user the user making the request
     * @return ChoreResponse the response
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public ChoreResponse makeChoreResponse(Chore chore, @Nullable User user) {
        UserChoreProgress.Progress progress = user == null ? null
                : userChoreProgressRepository.findUserChoreProgressByUserAndChore(user, chore)
                .map(UserChoreProgress::getProgress).orElse(null);

        LocalDate dueDate = DateHelper.localDateOfDate(chore.getDueDate());
        return new ChoreResponse(chore.getText(), dueDate, progress);
    }

    @PutMapping("/users/{userId}/chores/{choreId}/progress")
    @Transactional
    public void setChoreState(@PathVariable("choreId") Long choreId,
                              @PathVariable("userId") Long userId,
                              @RequestBody UserChoreProgress.Progress value,
                              @RequestHeader("Authorization") @Nullable String auth) {
        User user = tokenAuthService.assertTokenForUserAndGet(userId, auth);
        Chore chore = choreRepository.findById(choreId).orElseThrow(ChoreNotFoundException::new);

        Optional<UserChoreProgress> progressOpt = userChoreProgressRepository
                .findUserChoreProgressByUserAndChore(user, chore);

        progressOpt.ifPresentOrElse(it -> it.setProgress(value),
                () -> userChoreProgressRepository.save(new UserChoreProgress(user, chore, value)));
    }
}
