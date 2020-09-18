package com.kidzona.driverservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kidzona.driverservice.entity.Driver;
import com.kidzona.driverservice.entity.Kid;
import com.kidzona.driverservice.error.NotFoundException;
import com.kidzona.driverservice.error.TokenAuthenticationException;
import com.kidzona.driverservice.error.UserAlreadyRegisteredException;
import com.kidzona.driverservice.helper.TokenUtil;
import com.kidzona.driverservice.repository.DriverRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/driver")

public class DriverController {

	@Autowired
	private DriverRepository driverRepository;
	 @Autowired
	    TokenUtil tokenUtil;

	@GetMapping(value = { "", "/" })
	public ResponseEntity<List<Driver>> getAllDrivers() {
		List<Driver> result = driverRepository.findAll();
		System.out.println("yaaaaaaaaaaaaaaaaaaaaaarb");
		System.out.println(result) ; 
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}

	 @GetMapping("/{id}")
	    public Driver getParentById(@PathVariable int id) {
	        this.checkDriverWithIdExists(id);
	        return driverRepository.getOne(id);
	    }
	 
	 
		@PostMapping(value = { "/new" })
		public ResponseEntity<Driver> createNewDriver(@Valid @RequestBody Driver driver) {
			this.saveIfEmailNotRepeated(driver);
			Driver result = driverRepository.save(driver); 
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		}
	    
	    @PutMapping("/update")
	    public void updateDriverData(@Valid @RequestBody Driver driver) {
	        this.checkDriverWithIdExists(driver.getId());
	        this.saveIfEmailNotRepeated(driver);
	    }

	    
	    
	    @GetMapping("/user")
	    public HashMap<String, Integer> getdriverIdByUserId(@RequestHeader("${auth.header}") String token){
	        System.out.println(this.tokenUtil.getUserIdFromJWT(token));
	        int authUserId = this.tokenUtil.getUserIdFromJWT(token);

	        List<Driver> drivers = this.driverRepository.findByUserId(authUserId);
	        if(drivers.isEmpty())
	            throw new NotFoundException("This account hasn't registered driver data yet");
	        HashMap<String, Integer> response = new HashMap<>();
	        response.put("parentId", drivers.get(0).getId());
	        return response;
	    }
	    
 
	    @GetMapping("/{id}/kids/all")
	    public Set<Kid> getAllKidsByDriverId(@PathVariable int id/*, @RequestHeader("${auth.header}") String token*/) {
	      //  authenticateByUserId(id, token);
	    	System.out.println("yaaaaaaaaaaaaaaaaaaaaaarb");
	        return driverRepository.getOne(id).getKids();
	    }

	    private void saveIfEmailNotRepeated(Driver driver) {
	        List<Driver> listOfParentsWithEmail = this.driverRepository.findByEmail(driver.getEmail());
	        if(listOfParentsWithEmail.isEmpty() || listOfParentsWithEmail.get(0).getId() == driver.getId()){
	            this.driverRepository.save(driver);
	        } else {
	            throw new UserAlreadyRegisteredException("this email already exists");
	        }
	    }
	 
	      
    private void checkDriverWithIdExists(@PathVariable int id) {
        if (!this.driverRepository.existsById(id))
        	throw new NotFoundException("can't find driver with this id");
    }

}