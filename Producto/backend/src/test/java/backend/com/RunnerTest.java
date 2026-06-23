package backend.com;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class RunnerTest {

    public static void main(String[] args) {

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectPackage("backend.com.comercial"),
                        selectPackage("backend.com.gestionUsuarios"),
                        selectPackage("backend.com.shared"),
                        selectPackage("backend.com.produccion")
                )
                .build();

        Launcher launcher = LauncherFactory.create();

        launcher.registerTestExecutionListeners(new TestResultListener());

        launcher.execute(request);
    }
}