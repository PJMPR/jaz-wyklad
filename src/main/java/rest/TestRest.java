package rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dozer.DozerBeanMapper;

import contract.AddressSummaryDto;
import contract.PersonDto;
import domain.Address;
import domain.Person;

@Path("/test")
@Stateless
public class TestRest {

	@PersistenceContext
	EntityManager em;
	
	DozerBeanMapper mapper = new DozerBeanMapper();
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response test(){
		return Response.ok("working").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response AddPerson(Person person) {
		
		em.persist(person);
		
		return Response.ok().build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response GetPerson(@PathParam("id") int id) {
		Person result = em.find(Person.class, id);
		
		if(result==null)
			return Response.status(404).build();
		PersonDto response = mapper.map(result, PersonDto.class);
		return Response.ok(response).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{personId}")
	public Response AddAddress(@PathParam("personId")int personId, AddressSummaryDto addressDto) {

		Person person = em.find(Person.class, personId);
		if(person==null)
			return Response.status(404).build();
		Address address = new Address();
		address.setAddressLine(addressDto.getAddressLine());
		address.setOwner(person);
		person.getAddresses().add(address);
		em.persist(address);
		return Response.ok(address.getAddressLine()+"/"+addressDto.getAddressLine()).build();
	}
}
