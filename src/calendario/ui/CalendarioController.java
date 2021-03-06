package calendario.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import calendario.model.Calendario;
import calendario.model.ICalendario;
import calendario.repository.ICalendarioManager;
import common.ui.Alertas;
import common.ui.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nota.model.INota;
import nota.model.Nota;
import receta.repository.IRecetaManager;

//Controlador de la interfaz gráfica de la aplicación de calendario.

public class CalendarioController {
	
	private ICalendarioManager manager;
    private HashMap<String, Label> labels = new HashMap<>();

	//Elementos gráficos.

    @FXML
    private Button botonVolver;

    @FXML
    private Label labelDia;

    @FXML
    private Label labelSemana;

    @FXML
    private DatePicker calendario;

    @FXML
    private Label labelError;

    @FXML
    private ListView<String> tareas;

    @FXML
    private TextField texto;

    @FXML
    private Button botAgregar;

    @FXML
    private Button botEditar;

    @FXML
    private Button botBorrar;

	/**
	 * Añade un evento al manager de Calendario
	 * @param event
	 */
    @FXML
    void agregar(ActionEvent event) {
    	
    	if(!(texto.getText().equals("")) && !(calendario.getValue() == null)) {
    		tareas.getItems().add(texto.getText());
            Calendario cal = new Calendario(texto.getText(), calendario.getValue().toString());
            cal.setNombre(texto.getText());
            manager.create(cal);
            manager.saveAll();
            texto.setText("");
            labelError.setText("");
    	}
    	
    	else {
    		labelError.setText("Seleccione una fecha y escriba un evento");
    	}
    	
    }

	/**
	 * Permite seleccionar un evento para eliminar de la aplicacion
	 * @param event
	 */
    @FXML
    void borrar(ActionEvent event) {
    	labelError.setText("");
    	String tarea = tareas.getSelectionModel().getSelectedItem();
    	if(tarea == null) {
    		labelError.setText("Seleccione un evento de la lista");
    	}
    	else {
	    	ArrayList<Calendario> cals = manager.readAll();
	    	for (Calendario calendario : cals) {
	    		if(calendario.getNombre().equals(tarea)) {
	    			manager.remove(tarea);
	    			manager.saveAll();
	    		}
	    	}
	    	loadNotas();
    	}
    }

	/**
	 * Permite al usuario editar un evento de la aplicaion
	 * @param event
	 */
    @FXML
    void editar(ActionEvent event) {
    	labelError.setText("");
    	String tarea = tareas.getSelectionModel().getSelectedItem();
    	if(tarea == null) {
    		labelError.setText("Seleccione un evento de la lista");
    	}
    	else {
	    	ArrayList<Calendario> cals = manager.readAll();
	    	for (Calendario calendario : cals) {
	    		if(calendario.getNombre().equals(tarea)) {
	    			manager.remove(tarea);
	    			manager.saveAll();
	    		}
	    	}
	    	loadNotas();
	    	labelError.setText("");;
	    	texto.setText(tarea);

    	}
    	
    }

	/**
	 * Permite ver el dia de la semana
	 * @param event
	 */
    @FXML
    void verDia(ActionEvent event) {
    	String fecha = calendario.getValue().toString();
    	Vector<String> dias = new Vector<String>();
    	dias.add("Domingo");
    	dias.add("Lunes");
    	dias.add("Martes");
    	dias.add("Miercoles");
    	dias.add("Jueves");
    	dias.add("Viernes");
    	dias.add("Sabado");
    	
    	try {
    		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha); 
    		labelDia.setText(String.valueOf(date.getDate()));
        	labelSemana.setText(dias.get(date.getDay()));
    	}catch(Exception e) {
    		System.out.println("Error");
    	}
    	loadNotas();
    }

	/**
	 * Vuelve al menú principal
	 * @param event
	 */
    @FXML
    void volver(ActionEvent event) {

    	texto.setText("");
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(Scenes.getData().getSceneAgenda());
    }
	/**
	 * Asigna un CalendarioManager a la aplicación.
	 * @param manager CalendarioManager.
	 */
    public void setManager(ICalendarioManager manager) {
        this.manager = manager;
    }

	/**
	 * Lee los eventos contenidos en el archivo..
	 * @param event
	 */
	void loadNotas() {
    	tareas.getItems().clear();
    	labelError.setText("");
    	boolean encontrado = false;
    	int i;
        ArrayList<Calendario> cals = manager.readAll();
        for (Calendario calendario : cals) {
        	//System.out.println(calendario.getFecha());
//        	System.out.println(calendario.getFecha());
//        	System.out.println(calendario.getTexto());
//        	System.out.println(calendario.getNombre());
        	//manager.remove("A");
            if(calendario.getFecha().equals(this.calendario.getValue().toString())) {
                tareas.getItems().add(calendario.getTexto());
                encontrado = true;
            }
            //manager.saveAll();
        }
        if(encontrado == false) {
        	labelError.setText("No hay eventos para esta fecha");
        }
       

    }
}
