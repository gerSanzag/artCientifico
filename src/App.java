import controller.ArtCientificoController;
import view.ArtCientificoView;

public class App {
    public static void main(String[] args) {
        ArtCientificoView vista = new ArtCientificoView();
        ArtCientificoController controlador = new ArtCientificoController(vista);
        
        vista.mostrarMensaje("Bienvenido al Sistema de Gestión de Artículos Científicos");
        vista.mostrarMensaje("Esta aplicación permite ingresar información sobre artículos científicos");
        vista.mostrarMensaje("Los campos son opcionales, puede dejarlos en blanco si no desea ingresarlos");
        
        controlador.iniciar();
    }
}
