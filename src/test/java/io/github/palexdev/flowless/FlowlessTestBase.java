package io.github.palexdev.flowless;

import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

public class FlowlessTestBase extends ApplicationTest {

    protected Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }
}
