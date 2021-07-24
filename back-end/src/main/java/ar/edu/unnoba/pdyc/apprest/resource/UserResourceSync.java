package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.NewUserDTO;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResourceSync {
    @Autowired
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(NewUserDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(dto, User.class);
        try {
            userService.create(user);
            return Response.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
