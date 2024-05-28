package sd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sd.dtos.RegisterDTO;
import sd.entities.User;
import sd.services.UserService;

@SpringBootApplication
public class SdApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SdApplication.class, args);

		UserService userService = context.getBean(UserService.class);

		RegisterDTO adminDTO = new RegisterDTO();
		adminDTO.setEmail("admin@yahoo.com");
		adminDTO.setPassword("admin");
		adminDTO.setConfirmedPassword("admin");
		adminDTO.setRole("Admin");

		User existingAdmin = userService.getUserByEmail(adminDTO.getEmail());
		if (existingAdmin == null) {
			userService.register(adminDTO);
			System.out.println("Contul de administrator a fost creat cu succes!");
		} else {
			System.out.println("Un cont de administrator cu această adresă de email există deja.");
		}



/*
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;
		client = new MailjetClient("08ffbc0713a5a157b336452efe89f6fa", "f5fc2b5a528ebbb357bb3811739c0b4a");
		request = new MailjetRequest(Emailv31.resource)
				.property(Emailv31.MESSAGES, new JSONArray()
						.put(new JSONObject()
								.put(Emailv31.Message.FROM, new JSONObject()
										.put("Email", "andreeabode894@yahoo.com")
										.put("Name", "Andreea"))
								.put(Emailv31.Message.TO, new JSONArray()
										.put(new JSONObject()
												.put("Email", "andreeabode894@yahoo.com")
												.put("Name", "Andreea")))
								.put(Emailv31.Message.SUBJECT, "Greetings from Mailjet.")
								.put(Emailv31.Message.TEXTPART, "My first Mailjet email")
								.put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
								.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
		response = client.post(request);
		System.out.println(response.getStatus());
		System.out.println(response.getData());

	}*/
	}
}
