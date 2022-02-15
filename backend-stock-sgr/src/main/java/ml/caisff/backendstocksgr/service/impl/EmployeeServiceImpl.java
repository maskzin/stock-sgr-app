package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Employee;
import ml.caisff.backendstocksgr.repository.EmployeeRepository;
import ml.caisff.backendstocksgr.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getNom() != null) {
                    existingEmployee.setNom(employee.getNom());
                }
                if (employee.getPrenom() != null) {
                    existingEmployee.setPrenom(employee.getPrenom());
                }
                if (employee.getCreatedAt() != null) {
                    existingEmployee.setCreatedAt(employee.getCreatedAt());
                }
                if (employee.getUpdateAt() != null) {
                    existingEmployee.setUpdateAt(employee.getUpdateAt());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findEmployeesByDivisionId(Long id){
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findEmployeesByDivisionId(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
