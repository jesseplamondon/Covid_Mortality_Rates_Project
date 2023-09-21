package application;


import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.util.CoreMap;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class UI extends Application {
	private double WIDTH = 425, HEIGHT = 425;
	private VBox app, textbox;
	private HBox userspace;
	private ScrollPane textpane;
	private Text text;
	private TextField textfield;
	private Button send;
	private ChatBot cb;
	private Scene scene;
	
	@Override
	public void init() throws Exception{
		System.out.println("init");
	}
	
	@Override
	public void start(Stage stage){
		try {
			cb = new ChatBot("Mitch", "en");
			textfield = new TextField();
			send = new Button("Send");
			send.setDefaultButton(true);
			send.setOnAction(enter -> {
				if(textfield.getText().replaceAll("\\s+","").equals("")) {
					textfield.setText("");
				}
				else {
					text = new Text();
					text.setWrappingWidth(WIDTH);
					text.setFill(Color.GREEN);
					text.setText("User: " + textfield.getText());
					textbox.getChildren().add(text);
					if(textfield.getText().contains("bye".toLowerCase()))
						System.exit(0);
					text = new Text();
					text.setWrappingWidth(WIDTH);
					text.setFill(Color.RED);
					var ans = cb.sendPhrase(textfield.getText());
					var phrase = textfield.getText();
					String ner = cb.NER(phrase,ans);
					System.out.println(ner);
					text.setText("Bot: " + ner);
					textbox.getChildren().add(text);
					textfield.setText("");
				}
			});
			
			userspace = new HBox(textfield,send);
			userspace.setPrefSize(WIDTH, 25);
			textbox = new VBox();
			textbox.setPrefSize(WIDTH, HEIGHT*0.855);
			textpane = new ScrollPane();
			textpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			textpane.setContent(textbox);
			textpane.vvalueProperty().bind(textbox.heightProperty());
			app = new VBox(textpane,userspace);
			scene = new Scene(app,WIDTH,HEIGHT);
			
			stage.setScene(scene);
			stage.setTitle("Chatbot");
			stage.setWidth(WIDTH+25);
			stage.setHeight(HEIGHT);
			stage.setMinWidth(WIDTH+25);
			stage.setMaxWidth(WIDTH+25);
			stage.setMinHeight(HEIGHT);
			
			send.setPrefWidth(0.25*stage.getWidth());
			textfield.setPrefWidth(0.75*stage.getWidth());
			
			stage.heightProperty().addListener((obs) -> {
				textpane.setPrefHeight(stage.getHeight()-userspace.getHeight());
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("exit");
	}
}