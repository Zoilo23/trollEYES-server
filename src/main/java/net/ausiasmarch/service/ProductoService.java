package net.ausiasmarch.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import net.ausiasmarch.bean.ProductoBean;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.ProductoDao;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.setting.ConnectionSettings;

public class ProductoService extends GenericService implements ServiceInterface {

    String[] frasesInicio = {"Juego MMORPG ", "Juego Accion ", "Juego Aventura ", "Juego RPG  "};
    String[] frasesFinal = {"para xbox. ", "para PC. ", "para ps4. ", "para nintendo. "};
    String[] imagesRandom = {"https://i.etsystatic.com/6867090/r/il/1c2046/1775260633/il_794xN.1775260633_c1tp.jpg", "https://i.pinimg.com/originals/29/c3/c3/29c3c3fa9526d19299155f62d6097ba0.jpg", "https://i.pinimg.com/564x/63/38/bc/6338bccf2150bdea31d1053084c2caf6.jpg", "https://i.pinimg.com/564x/6a/5a/d9/6a5ad90ad2b791199300cd6aa4929317.jpg"};

    public ProductoService(HttpServletRequest oRequest) {
        super(oRequest);
    }

    public String fill() throws Exception {
        ConnectionInterface oConnectionImplementation = ConnectionFactory
                .getConnection(ConnectionSettings.connectionPool);
        Connection oConnection = oConnectionImplementation.newConnection();
        ProductoDao oProductoDao = new ProductoDao(oConnection);
        Gson oGson = GsonFactory.getGson();
        int numProd = Integer.parseInt(oRequest.getParameter("number"));
        for (int i = 0; i < numProd; i++) {
            ProductoBean oProductoBean = new ProductoBean();
            int numAleatorio = (int) Math.floor(Math.random() * (100000 - 999999) + 999999);
            int numAleatorio1 = (int) Math.floor(Math.random() * (0 - 999) + 999);
            double numAleatorio2 = (double) Math.random() * (0 - 999) + 999;
            DecimalFormat format2 = new DecimalFormat("#,00");
            double precioAleatorio = Double.parseDouble(format2.format(numAleatorio2));
            int alTipoProducto_id = (int) Math.floor(Math.random() * 12) + 1;
            oProductoBean.setCodigo(numAleatorio + "");
            oProductoBean.setExistencias(numAleatorio1);
            oProductoBean.setPrecio(precioAleatorio);
            oProductoBean.setImagen(generaImages(1));
            oProductoBean.setDescripcion(generaTexto(1));
            oProductoBean.setTipo_producto_id(alTipoProducto_id);
            oProductoDao.insert(oProductoBean);
        }
        ResponseBean oResponseBean = new ResponseBean(200, "Insertados los registros con exito");
        if (oConnection != null) {
            oConnection.close();
        }
        if (oConnectionImplementation != null) {
            oConnectionImplementation.disposeConnection();
        }
        return oGson.toJson(oResponseBean);
    }

    private String generaTexto(int longitud) {
        String fraseRandom = "";
        for (int i = 0; i < longitud; i++) {
            fraseRandom += frasesInicio[(int) (Math.random() * frasesInicio.length) + 0];
            fraseRandom += frasesFinal[(int) (Math.random() * frasesFinal.length) + 0];
        }
        return fraseRandom;
    }
    
    private String generaImages(int longitud) {
        String imageRandom = "";
        for (int i = 0; i < longitud; i++) {
            imageRandom += imagesRandom[(int) (Math.random() * imagesRandom.length) + 0];
        }
        return imageRandom;
    }
}
