package flashcards.ui;

import common.ui.Alertas;
import common.ui.Scenes;
import flashcards.model.Flashcards;
import flashcards.model.IFlashcard;
import flashcards.repository.IFlashcardManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

//Controlador de la interfaz gráfica de la aplicación de flashcards.

public class FlashcardController {
    Slider slider = new Slider();
    private IFlashcardManager manager;
    private HashMap<String, Label> labels = new HashMap<>();

    //Elementos gráficos.

    @FXML
    private Button buttonExit;

    @FXML
    private ListView<String> panelFlashcard;

    @FXML
    private Button buttonAddFlashcard;

    @FXML
    private Button buttonRemoveFlashcard;

    @FXML
    private Button buttonReturnToMenu;

    @FXML
    private Button buttonLoadFlashcard;

    @FXML
    private Button buttonSaveFlashcard;
    /**
     * Añade una flashcard al Manager.
     * @param event
     */
    @FXML
    void addFlashcard(ActionEvent event) {

        String nombre = Alertas.showInputDialog("Introduce el titulo de la flashcard: ", "Flashcard", "");
        String contenido= Alertas.showInputDialog("Introduce el contenido de la flashcard:", "Contenido", "");
        Flashcards flashcard = new Flashcards(nombre,contenido);
        flashcard.setNombre(nombre);
        flashcard.setContenido(contenido);
        if(manager.create(flashcard)) {
            drawFlashcard(flashcard);
        }
    }

    /**
     * Pregunta al usuario si quiere guardar las flashcards antes de salir.
     */
    private void askToSave() {
        if(Alertas.showYesNoDialog("¿Desea guardar las flashcards?", "Guardar flashcards")) {
            saveFlashcard(new ActionEvent());
        }
    }
    /**
     * Lee las flashcards contenidas en el archivo.
     */
    @FXML
    void loadFlashcard() {
        panelFlashcard.getItems().clear();
        ArrayList<IFlashcard> flashcards = manager.readAll();
        for (IFlashcard flashcard  : flashcards) {
            if(!labels.containsKey(flashcard.getNombre()))
                drawFlashcard(flashcard);
        }
    }

    /**
     * Muestra el contenido de la flashcard seleccionada
     */
    @FXML
    void verFlashcard() {
        panelFlashcard.getItems().clear();
        ArrayList<IFlashcard> flashcards = manager.readAll();
        for (IFlashcard flashcard  : flashcards) {
            if(!labels.containsKey(flashcard.getNombre()))
                slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Alertas.showInputDialog("Respuesta",flashcard.getNombre(),"");
                    }
                });
        }
    }/**
     * Dibuja las flashcards en pantalla.
     * @param flashcard Flashcard que se va a dibujar.
     */
    private void drawFlashcard(IFlashcard flashcard) {
        panelFlashcard.getItems().add(flashcard.getNombre());
    }

    /**
     * Permite acceder a la flashcard seleccionada para ver su contenido.
     */
    public void makePanelClickable() {
        panelFlashcard.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String nombreFlashcard = panelFlashcard.getSelectionModel().getSelectedItem();
                String contenidoFlashcard = manager.read(nombreFlashcard).getContenido();
                Alertas.showInfo(contenidoFlashcard, nombreFlashcard + ":");
            }
        });
    }

    /**
     * Borra una flashcard de la pantalla.
     * @param name Nombre de la flashcard.
     */
    private void eraseFlashcard(String name) {
        panelFlashcard.getItems().remove(labels.get(name));
        loadFlashcard();
    }
    /**
     * Permite al usuario seleccionar una flashcard para eliminar de la aplicación.
     * @param event
     */
    @FXML
    void removeFlashcard(ActionEvent event) {
        ArrayList<String> allNotes = new ArrayList<>();

        for (IFlashcard flashcard : manager.readAll()) {
            allNotes.add(flashcard.getNombre());
        }
        String nombre = Alertas.showChoiceDialog(allNotes, "Selecciona la flashcard que quieres borrar",
                                                             "Eliminar flashcard");
        manager.remove(nombre);
        eraseFlashcard(nombre);
    }
    /**
     * Vuelve al menú principal, preguntando antes al usuario si desea guardar las flashcards.
     * @param event
     */
    @FXML
    void returnToMenu(ActionEvent event) {

        askToSave();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(Scenes.getData().getSceneAgenda());
    }
    /**
     * Guarda las flashcards en el manager
     * @param event
     */
    @FXML
    void saveFlashcard(ActionEvent event) {
        manager.saveAll();
    }
    /**
     * Asigna un FlashcardManager a la aplicación.
     * @param manager FlashcardManager.
     */
    public void setManager(IFlashcardManager manager) {
        this.manager = manager;
    }
}
