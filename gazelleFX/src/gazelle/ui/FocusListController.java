package gazelle.ui;

import gazelle.api.ChoreFullResponse;
import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

public class FocusListController extends BaseController {

    @FXML
    private VBox list;

    private final GazelleController app;


    private FocusListController(GazelleController app) {
        this.app = app;
    }

    public void onShow() {
        list.getChildren().clear();
        app.sideRun(() -> {
            try {
                List<ChoreFullResponse> chores = app.getClient().chores()
                        .getFocusedChores(app.getClient().loggedInUserId());
                app.mainRun(() -> {
                    for (ChoreFullResponse chore : chores) {
                        FocusChoreController fcc = FocusChoreController.load(app);
                        fcc.setChore(chore);
                        list.getChildren().add(fcc.getNode());
                    }
                });
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke laste fokuslista", e.getMessage());
                    app.showMyCourses();
                });
            }
        });


    }

    public static FocusListController load(GazelleController app) {
        return loadFromFXML("/scenes/focusList.fxml", new FocusListController(app));
    }
}

