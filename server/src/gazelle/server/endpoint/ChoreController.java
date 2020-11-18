package gazelle.server.endpoint;

import gazelle.api.ChoreResponse;
import gazelle.api.NewChoreRequest;
import gazelle.api.ValueWrapper;
import gazelle.model.Chore;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.model.UserChoreProgress;
import gazelle.server.error.ChoreNotFoundException;
import gazelle.server.error.UnprocessableEntityException;
import gazelle.server.repository.ChoreRepository;
import gazelle.server.service.ChoreProgressService;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class ChoreController {

    private final ChoreRepository choreRepository;
    private final ChoreProgressService choreProgressService;

    private final TokenAuthService tokenAuthService;

    public ChoreController(ChoreRepository choreRepository,
                           ChoreProgressService choreProgressService,
                           TokenAuthService tokenAuthService) {
        this.choreRepository = choreRepository;
        this.choreProgressService = choreProgressService;
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
        ChoreResponse.Builder builder = new ChoreResponse.Builder();
        builder.id(chore.getId())
                .key(chore.getKey())
                .text(chore.getText())
                .dueDate(DateHelper.localDateOfDate(chore.getDueDate()));

        if (user != null)
            builder.progress(choreProgressService.getProgress(user, chore));

        return builder.build();
    }

    /**
     * Creates a new Chore based on a NewChoreRequest
     *
     * @param r the NewChoreRequest
     * @param post the post the chore will belong to
     * @return Chore the created Chore object
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Chore buildChore(NewChoreRequest r, Post post) {
        r.validate();
        if (r.getId() != null)
            throw new IllegalArgumentException();
        Chore response = new Chore(r.getKey(),
                r.getText(),
                DateHelper.dateOfLocalDate(r.getDueDate()),
                post);
        return choreRepository.save(response);
    }

    /**
     * Updates an existing chore based on a NewChoreRequest
     *
     * @param r the NewChorRequest
     * @param chore the Chore object
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateChore(NewChoreRequest r, Chore chore) {
        r.validate();
        if (!Objects.equals(chore.getId(), r.getId()))
            throw new IllegalArgumentException();
        chore.setKey(r.getKey());
        chore.setText(r.getText());
        chore.setDueDate(DateHelper.dateOfLocalDate(r.getDueDate()));
    }

    @PutMapping("/users/{userId}/chores/{choreId}/progress")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void setChoreState(@PathVariable("choreId") Long choreId,
                              @PathVariable("userId") Long userId,
                              @RequestBody ValueWrapper<UserChoreProgress.Progress> value,
                              @RequestHeader(name = "Authorization", required = false)
                                  @Nullable String auth) {
        User user = tokenAuthService.assertTokenForUserAndGet(userId, auth);
        Chore chore = choreRepository.findById(choreId).orElseThrow(ChoreNotFoundException::new);

        if (value.getValue() == null)
            throw new UnprocessableEntityException();

        choreProgressService.setProgress(user, chore, value.getValue());
    }
}
