package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

/**
 * 도메인 경계 규칙 테스트
 *
 * 각 도메인은 독립적이어야 하며, 다른 도메인에 직접 의존하면 안됨
 *
 * freeze() 기능을 사용하여 현재 위반 사항을 기록하고,
 * 새로운 위반만 탐지합니다.
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class DomainBoundaryTest {

    // ============================================================
    // DOM-001: 도메인 간 순환 의존성 금지
    // ============================================================

    @ArchTest
    static final ArchRule domains_should_be_free_of_cycles =
        freeze(SlicesRuleDefinition.slices()
            .matching("com.mzc.backend.lms.domains.(*)..")
            .should().beFreeOfCycles()
            .because("도메인 간 순환 의존성이 있으면 안됩니다"));
}
