package com.vet24.web.controllers.user;

import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.user.Client;
import com.vet24.models.user.Comment;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/client/doctor")
@Tag(name = "doctor-controller", description = "operations with doctors")
public class ClientCommentController {

    private final DoctorService doctorService;
    private final ClientService clientService;
    private final CommentService commentService;

    @Autowired
    public ClientCommentController(DoctorService doctorService, ClientService clientService, CommentService commentService) {
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.commentService = commentService;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/{doctorId}/addComment")
    public ResponseEntity<String> persistComment(@PathVariable("doctorId") Long doctorId, String text){
        Doctor doctor = doctorService.getByKey(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Comment comment = null;
            Client currentClient = clientService.getCurrentClient();
            Long clientId = currentClient.getId();
            if (commentService.findByClientIdAndDoctorId(clientId, doctorId) == null) {
                comment = new Comment(
                        clientService.getCurrentClient(), text, LocalDate.now(), doctor
                );
                commentService.persist(comment);
            } else {
                throw new RepeatedCommentException("You can add only one comment to Doctor. So you have to update or delete old one.");
            }
            return new ResponseEntity<>(comment.getContent(), HttpStatus.OK);
        }
    }
}
