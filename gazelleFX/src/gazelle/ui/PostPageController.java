package gazelle.ui;

public class PostPageController extends BaseController {

    private final GazelleController app;
    private Long postId;

    private PostPageController(GazelleController app) {
        this.app = app;
    }

    public void onShow(Long postId) {
        this.postId = postId;
    }

    public static PostPageController load(GazelleController app) {
        return loadFromFXML("/scenes/postPage.fxml", new PostPageController(app));
    }
}
