package gazelle.server.service;

import gazelle.model.Chore;
import gazelle.model.User;
import gazelle.model.UserChoreProgress;
import gazelle.server.repository.UserChoreProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChoreProgressService {

    private final UserChoreProgressRepository userChoreProgressRepository;

    @Autowired
    public ChoreProgressService(UserChoreProgressRepository userChoreProgressRepository) {
        this.userChoreProgressRepository = userChoreProgressRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public UserChoreProgress.Progress getProgress(User user, Chore chore) {
        return userChoreProgressRepository.findUserChoreProgressByUserAndChore(user, chore)
                .map(UserChoreProgress::getProgress).orElse(UserChoreProgress.Progress.UNDONE);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void setProgress(User user, Chore chore, UserChoreProgress.Progress value) {
        Optional<UserChoreProgress> progressOpt = userChoreProgressRepository
                .findUserChoreProgressByUserAndChore(user, chore);

        progressOpt.ifPresentOrElse(it -> it.setProgress(value),
                () -> userChoreProgressRepository.save(new UserChoreProgress(user, chore, value)));
    }
}
