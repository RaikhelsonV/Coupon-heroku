package com.example.MyProject.rest;


import com.example.MyProject.entity.Client;
import com.example.MyProject.entity.Company;
import com.example.MyProject.entity.Customer;
import com.example.MyProject.entity.User;
import com.example.MyProject.exceptions.InvalidLoginException;
import com.example.MyProject.exceptions.InvalidSessionTException;
import com.example.MyProject.repository.UserRepository;
import com.example.MyProject.service.AdminService;
import com.example.MyProject.service.CompanyService;
import com.example.MyProject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSystem {

    private ApplicationContext context;
    private UserRepository userRepository;
    private Thread dailyTask;
    private Thread tokenTask;

    @Autowired
    public UserSystem(ApplicationContext context, UserRepository userRepository, DailyTask dailyTask, TokenTask tokenTask) {
        this.context = context;
        this.userRepository = userRepository;
        this.dailyTask = new Thread(dailyTask);
        this.tokenTask = new Thread(tokenTask);
    }
    @PostConstruct
    private void startTasks(){
        dailyTask.start();
    }
    @PreDestroy
    private void stopTasks(){
        DailyTask.stopDailyTask();
        TokenTask.stopTokenTask();
        System.out.println("@PreDestroy method");
    }
    public ClientSession createClientSession(String email, String password)  {
        if (!tokenTask.isAlive()) {
            tokenTask.start();
        }
        String adminEmail = "admin@mail";
        String adminPassword = "111";

        if (email.equals(adminEmail) && password.equals(adminPassword)) {
            AdminService adminService = context.getBean(AdminService.class);
            ClientSession clientSession = context.getBean(ClientSession.class);
            clientSession.setRole(3);
            clientSession.setAdminService(adminService);
            clientSession.accessed();
            return clientSession;
        }

        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (!optionalUser.isPresent()) {
            throw new InvalidLoginException(String.format("Invalid login with email: %s and password: %s", email, password));
        }
        Client client = optionalUser.get().getClient();
        if (client instanceof Customer) {
            return getCustomerSession(client);
        } else if (client instanceof Company) {
            return getCompanySession(client);
        }
        throw new InvalidSessionTException("The session is invalid ");
    }
    private ClientSession getCustomerSession(Client client) {
        CustomerService customerService = context.getBean(CustomerService.class);
        customerService.setCustomer_id(client.getId());
        ClientSession clientSession = context.getBean(ClientSession.class);
        clientSession.setRole(1);
        clientSession.setCustomerService(customerService);
        clientSession.accessed();
        return clientSession;
    }
    private ClientSession getCompanySession(Client client) {
        CompanyService companyService = context.getBean(CompanyService.class);
        companyService.setCompany_id(client.getId());
        ClientSession clientSession = context.getBean(ClientSession.class);
        clientSession.setRole(2);
        clientSession.setCompanyService(companyService);
        clientSession.accessed();
        return clientSession;
    }



}


