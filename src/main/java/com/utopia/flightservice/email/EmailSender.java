package com.utopia.flightservice.email;

import javax.mail.MessagingException;

import com.utopia.flightservice.entity.Flight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
public class EmailSender {

//    @Value("${aws.emailSender}")
//    private String sender;
    private String sender = "anthony.sirimarco@smoothstack.com";

    private Region region = Region.US_EAST_2;

    public void sendFlightDetails(String recipientEmail, Flight flight) {
        String subject = "Utopia Airlines Flight Details";
        String recipient = recipientEmail;
        String bodyHtml = "<html>"
                + "<h1>Departure Info</h1>"
                +  "<p>" + flight.getRoute().getOriginAirport().getCity()
                + " (" + flight.getRoute().getOriginAirport().getIataId() + ") "
                + "at " + flight.getDepartureTime() + "</p>"
                + "<p><b>Gate:</b> (Coming soon)"
                + "<h1>Arrival Info</h1>"
                + "<p>" + flight.getRoute().getDestinationAirport().getCity()
                + " (" + flight.getRoute().getDestinationAirport().getIataId() + ") "
                + "at " + flight.getArrivalTime().toString().replace('T', ' ') + "</p>"
                + "<p><b>Gate:</b> (Coming soon)"
                + "</html>";

        String bodyText = "Departure Info \n"
                + flight.getRoute().getOriginAirport().getCity()
                + " (" + flight.getRoute().getOriginAirport().getIataId() + ")\n"
                + "at " + flight.getDepartureTime() + "\n"
                + "Arrival Info \n"
                + flight.getRoute().getDestinationAirport().getCity()
                + " (" + flight.getRoute().getDestinationAirport().getIataId() + ")\n"
                + "at " + flight.getArrivalTime() + "\n"
                + "";

        SesClient client = SesClient.builder().region(region).build();

        try {
            send(client, sender, recipient, subject, bodyHtml, bodyText);
            client.close();
            System.out.println("closed client........");
        }
        catch (MessagingException e){
            e.getStackTrace();
        }
    }

    private void send(SesClient client, String sender, String recipient,
                      String subject, String bodyHtml, String bodyText) throws MessagingException {

        Destination destination = Destination.builder().toAddresses(recipient).build();

        Content content = Content.builder().data(bodyHtml).build();

        Content sub = Content.builder().data(subject).build();

        Body body = Body.builder().html(content).build();

        Message message = Message.builder().subject(sub).body(body).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(sender)
                .build();

        try {
            System.out.println("Attempting to send email through SES");
            client.sendEmail(request);
        } catch (SesException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }
}
