package lt.vu.security.controller;

import lt.vu.security.employee.Employee;
import lt.vu.security.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api/v1/crud")
@RestController
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/addemployee")
    public Employee addEmployee(@RequestBody Employee employee){
        return service.saveEmployee(employee);
    }

    @GetMapping("/employees")
    public List<Employee> findAllEmployees(){
        return service.getAllEmployees();
    }

    @GetMapping("/employee/{id}")
    public Employee findEmployeeById(@PathVariable Long id){
        return service.findEmployeeById(id);
    }

    @PutMapping("/update/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee){
        return service.updateEmployee(id, employee);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id){
        return service.deleteEmployee(id);
    }
}
