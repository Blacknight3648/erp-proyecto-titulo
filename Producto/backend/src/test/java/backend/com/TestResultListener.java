package backend.com;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.util.concurrent.atomic.AtomicInteger;

public class TestResultListener implements TestExecutionListener {

    private final AtomicInteger total = new AtomicInteger();
    private final AtomicInteger passed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger skipped = new AtomicInteger();

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

        if (!testIdentifier.isTest()) return;

        total.incrementAndGet();

        switch (testExecutionResult.getStatus()) {
            case SUCCESSFUL -> passed.incrementAndGet();
            case FAILED -> failed.incrementAndGet();
            case ABORTED -> skipped.incrementAndGet();
        }
    }

    @Override
    public void testPlanExecutionFinished(org.junit.platform.launcher.TestPlan testPlan) {

        System.out.println("\n========== MATRIZ DE TEST ==========");
        System.out.println("TOTAL  : " + total.get());
        System.out.println("PASSED : " + passed.get());
        System.out.println("FAILED : " + failed.get());
        System.out.println("SKIPPED: " + skipped.get());
        System.out.println("===================================\n");
    }
}
