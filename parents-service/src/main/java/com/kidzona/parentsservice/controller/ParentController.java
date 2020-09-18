package com.kidzona.parentsservice.controller;


import com.kidzona.parentsservice.entity.Kid;
import com.kidzona.parentsservice.entity.Parent;
import com.kidzona.parentsservice.exception.KidNotFoundException;
import com.kidzona.parentsservice.exception.ParentNotFoundException;
import com.kidzona.parentsservice.exception.TokenAuthenticationException;
import com.kidzona.parentsservice.exception.UserAlreadyRegisteredException;
import com.kidzona.parentsservice.helper.TokenUtil;
import com.kidzona.parentsservice.repository.KidRepo;
import com.kidzona.parentsservice.repository.ParentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/parent")
@CrossOrigin
public class ParentController {

    @Autowired
    private ParentRepo parentRepo;

    @Autowired
    private KidRepo kidRepo;

    @Autowired
    TokenUtil tokenUtil;

    @GetMapping("/{id}")
    public Parent getParentById(@PathVariable int id) {
        this.checkParentWithIdExists(id);
        return parentRepo.getOne(id);
    }

    @PostMapping("/new")
    public HashMap<String, String> saveNewParent(@Valid @RequestBody Parent parent, @RequestHeader("${auth.header}") String token) {
        int userId = tokenUtil.getUserIdFromJWT(token);
        parent.setUserId(userId);
        this.saveIfEmailNotRepeated(parent);
        String parentIdString = String.valueOf(this.parentRepo.findByUserId(userId).get(0).getId());
        HashMap<String, String> result = new HashMap<>();
        result.put("parentId", parentIdString);
        return result;
    }

    @PutMapping("/update")
    public void updateParentData(@Valid @RequestBody Parent parent, @RequestHeader("${auth.header}") String token) {
        this.checkParentWithIdExists(parent.getId());
        this.saveIfEmailNotRepeated(parent);
    }

    @GetMapping("/{id}/kids/all")
    public Set<Kid> getAllKidsByParentId(@PathVariable int id, @RequestHeader("${auth.header}") String token) {
        authenticateByUserId(id, token);
        return parentRepo.getOne(id).getKids();
    }

    @GetMapping("/{parentId}/kids/{kidId}")
    public Kid getKidById(@PathVariable int parentId, @PathVariable int kidId, @RequestHeader("${auth.header}") String token) {
        authenticateByUserId(parentId, token);
        for (Kid kid : this.parentRepo.getOne(parentId).getKids()) {
            if (kid.getId() == kidId) return kid;
        }

        throw new KidNotFoundException("A kid with this id is not found");
    }

    @PostMapping("/{parentId}/kids/new")
    public void addNewKid(@PathVariable int parentId, @RequestHeader("${auth.header}") String token, @RequestBody @Valid Kid kid){
        authenticateByUserId(parentId, token);
        kid.setParent(parentRepo.getOne(parentId));
        kidRepo.save(kid);
    }


    @GetMapping("/user")
    public HashMap<String, Integer> getParentIdByUserId(@RequestHeader("${auth.header}") String token){
        System.out.println(this.tokenUtil.getUserIdFromJWT(token));
        int authUserId = this.tokenUtil.getUserIdFromJWT(token);

        List<Parent> parents = this.parentRepo.findByUserId(authUserId);
        if(parents.isEmpty())
            throw new ParentNotFoundException("This account hasn't registered parent data yet");
        HashMap<String, Integer> response = new HashMap<>();
        response.put("parentId", parents.get(0).getId());
        return response;
    }

    private void authenticateByUserId(int parentId, String token) {
        int userId = tokenUtil.getUserIdFromJWT(token);
        List<Parent> parents = this.parentRepo.findByUserId(userId);
        if(parents.isEmpty() || parents.get(0).getId() != parentId){
            throw new TokenAuthenticationException("Invalid Token");
        }
    }

    private void saveIfEmailNotRepeated(Parent parent) {
        List<Parent> listOfParentsWithEmail = this.parentRepo.findByEmail(parent.getEmail());
        if(listOfParentsWithEmail.isEmpty() || listOfParentsWithEmail.get(0).getId() == parent.getId()){
            this.parentRepo.save(parent);
        } else {
            throw new UserAlreadyRegisteredException("this email already exists");
        }
    }

    private void checkParentWithIdExists(@PathVariable int id) {
        if (!this.parentRepo.existsById(id))
            throw new ParentNotFoundException("can't find parent with this id");
    }
}
