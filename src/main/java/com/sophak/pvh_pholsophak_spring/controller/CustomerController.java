package com.sophak.pvh_pholsophak_spring.controller;

import com.sophak.pvh_pholsophak_spring.modal.Customer;
import com.sophak.pvh_pholsophak_spring.modal.Respon;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = ("/api/v1/customers"))
public class CustomerController {
    public static int id =1;
    ArrayList<Customer> customers = new ArrayList<>();
    public CustomerController(){
        customers.add(new Customer(id++,"koko","male",22,"PhnomPenh"));
        customers.add(new Customer(id++,"alex","male",22,"KohKong"));
        customers.add(new Customer(id++,"thida","female",22,"SiemReap"));
    }
    @GetMapping("/getAllCustomer")
    public ArrayList<Customer> readCustomer(){

        return  customers;
    }
    //insert new customer
    @PostMapping("/insertCustomer")
    public ArrayList<Customer> insertNewCustomer(@RequestBody Respon respon){

        customers.add(new Customer(id++,respon.getName(),respon.getGender(),respon.getAge(),respon.getAddress()));
        return customers;
    }

    //Search by id
    @GetMapping("/seachCustomerbyId/{id}")
    public Customer searchCutomerById(@PathVariable int id){
        for (Customer customer: customers){
            if (id== customer.getId()){
                return customer;
            }
        }
        return null;
    }
    //search customer by name
    @GetMapping("/searchCustomerByNama/{name}")
    public Customer searchCustomerByName(@RequestParam String name ){
        for (Customer customer:customers){
            if (customer.getName().equals(name)){
                return customer;
            }
        }
        return null;
    }


    @DeleteMapping("/removeCustomerById/{id}")
    public ArrayList<Customer> removeCutomerById (@PathVariable int id){
        for (int i = 0; i < customers.size(); i++) {
            if (id == customers.get(i).getId()){
                customers.remove(i);
                return customers;
            }
        }
        return null;
    }
    @PutMapping("/updateCustomerById/{id}")
    public ArrayList<Customer> upadteById(@PathVariable int id ,@RequestBody Respon respon){
        for (int i = 0; i < customers.size() ; i++) {
            if (id == customers.get(i).getId()){
                customers.get(i).setName(respon.getName());
                customers.get(i).setGender(respon.getGender());
                customers.get(i).setAge(respon.getAge());
                customers.get(i).setAddress(respon.getAddress());
            return customers;
            }

        }
        return null;
    }
}
