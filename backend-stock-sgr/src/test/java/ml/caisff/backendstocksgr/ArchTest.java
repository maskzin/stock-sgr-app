package ml.caisff.backendstocksgr;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ml.caisff.backendstocksgr");

        noClasses()
            .that()
            .resideInAnyPackage("ml.caisff.backendstocksgr.service..")
            .or()
            .resideInAnyPackage("ml.caisff.backendstocksgr.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ml.caisff.backendstocksgr.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
