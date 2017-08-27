package com.journaldev.spring.controller;

import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_CERTIFICATE_FILENAME;
import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_MQTT_CLIENT_ID;
import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_MQTT_HOST;
import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_MQTT_PORT;
import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_PRIVATE_KEY_FILENAME;
import static de.ozzc.iot.util.IoTConfig.ConfigFields.AWS_IOT_ROOT_CA_FILENAME;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.journaldev.spring.model.User;
import com.piyush.callback.ExampleCallback;

import de.ozzc.iot.util.IoTConfig;
import de.ozzc.iot.util.SslUtil;

@Controller
public class HomeController {

	MqttClient client = null;
	int QOS_LEVEL = 0;
	String TOPIC = "$aws/things/thing2/shadow/update";
	String MESSAGE = "{\r\n" + "\"test\":\"updated\"\r\n" + "\r\n" + "}";
	long QUIESCE_TIMEOUT = 5000;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		System.out.println("Home Page Requested, locale = " + locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String user(@Validated User user, Model model) throws Exception {
		System.out.println("User Page Requested");
		model.addAttribute("userName", user.getUserName());

		client.publish(TOPIC, new MqttMessage(MESSAGE.getBytes()));

		return "user";
	}

	HomeController() throws Exception {

		URL location = HomeController.class.getProtectionDomain().getCodeSource().getLocation();

		// IoTConfig("C:\\workspaces\\AWS_POC\\aws-iot_with_rpi-gpio\\src\\main\\resources\\config-example.properties");
		IoTConfig config = new IoTConfig("/var/lib/tomcat8/webapps/ROOT/WEB-INF/classes/config-example.properties");

		SSLSocketFactory sslSocketFactory = SslUtil.getSocketFactory(config.get(AWS_IOT_ROOT_CA_FILENAME),
				config.get(AWS_IOT_CERTIFICATE_FILENAME), config.get(AWS_IOT_PRIVATE_KEY_FILENAME));

		System.out.println("created sslSocketFactory");

		MqttConnectOptions options = new MqttConnectOptions();
		options.setSocketFactory(sslSocketFactory);
		options.setCleanSession(true);

		final String serverUrl = "ssl://" + config.get(AWS_IOT_MQTT_HOST) + ":" + config.get(AWS_IOT_MQTT_PORT);
		final String clientId = config.get(AWS_IOT_MQTT_CLIENT_ID);

		System.out.println("server url: " + serverUrl);
		System.out.println("client id: " + clientId);

		
		MemoryPersistence persistence = new MemoryPersistence();
		
		client = new MqttClient(serverUrl, clientId,persistence);

		client.setCallback(new ExampleCallback());
		client.connect(options);
	}

}