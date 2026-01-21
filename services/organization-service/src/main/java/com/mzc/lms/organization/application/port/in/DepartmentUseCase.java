package com.mzc.lms.organization.application.port.in;

import com.mzc.lms.organization.adapter.in.web.dto.DepartmentCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.DepartmentUpdateRequest;
import com.mzc.lms.organization.domain.model.Department;

import java.util.List;

public interface DepartmentUseCase {

    Department createDepartment(DepartmentCreateRequest request);

    Department updateDepartment(Long id, DepartmentUpdateRequest request);

    void deleteDepartment(Long id);

    Department getDepartment(Long id);

    Department getDepartmentByCode(String code);

    List<Department> getAllDepartments();

    List<Department> getDepartmentsByCollege(Long collegeId);
}
