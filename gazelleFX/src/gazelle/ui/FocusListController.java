package gazelle.ui;

import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

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
                app.getClient();
            } catch (ClientException e) {
                FxUtils.showAndWaitError("Klarte ikke laste fokuslista", e.getMessage());
            }
        });
    }

    public static FocusListController load(GazelleController app) {
        return loadFromFXML("/scenes/focusList.fxml", new FocusListController(app));
    }
}

