package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PrintController;
import ar.com.gtsoftware.api.exception.FileGenerationException;
import ar.com.gtsoftware.api.exception.FileNotFoundException;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.RemitoService;
import ar.com.gtsoftware.service.VentasService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ar.com.gtsoftware.enums.Parametros.*;

@RestController
@RequiredArgsConstructor
public class PrintControllerImpl implements PrintController {

    private final VentasService ventasService;
    private final ParametrosService parametrosService;
    private final RemitoService remitoService;
    private final HttpServletResponse response;
    private final ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(PrintControllerImpl.class);


    @Override
    public void getSaleBudget(Long saleId) {
        ComprobantesDto comprobante = ventasService.obtenerComprobante(saleId);
        if (comprobante == null) {
            handleEntityNotFound("saleId", saleId);
        }
        List<ComprobantesDto> ventas = Collections.singletonList(comprobante);
        boolean mostrarDetallePrecios = parametrosService.getBooleanParam(PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(ventas);
        Resource resource = resourceLoader.getResource("classpath:reports/presupuesto.jasper");

        Map<String, Object> parameters = loadCompanyParameters();
        parameters.put(PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS.getNombreParametro(), mostrarDetallePrecios);

        String fileName = String.format("venta-%d", saleId);

        handlePDFExport(fileName, beanCollectionDataSource, resource, parameters);

    }

    @Override
    public void getInvoice(Long saleId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void getDeliveryNote(Long deliveryNoteId) {
        RemitoDto remito = remitoService.find(deliveryNoteId);
        if (remito == null) {
            handleEntityNotFound("deliveryNoteId", deliveryNoteId);
        }
        List<RemitoDto> remitos = Collections.singletonList(remito);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(remitos);
        Resource resource = resourceLoader.getResource("classpath:reports/remito.jasper");

        Map<String, Object> parameters = loadCompanyParameters();

        String fileName = String.format("remito-%d", deliveryNoteId);

        handlePDFExport(fileName, beanCollectionDataSource, resource, parameters);
    }

    private void handlePDFExport(String fileName,
                                 JRBeanCollectionDataSource beanCollectionDataSource,
                                 Resource resource,
                                 Map<String, Object> parameters) {

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), parameters, beanCollectionDataSource);

            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.pdf", fileName));
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);

            ServletOutputStream servletStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);
        } catch (JRException | IOException e) {
            logger.error("Error al generar reporte", e);
            throw new FileGenerationException();
        }
    }

    private void handleEntityNotFound(String fieldId, Long id) {
        logger.error("Entity not found with {}={}", fieldId, id);
        throw new FileNotFoundException();
    }

    private Map<String, Object> loadCompanyParameters() {
        final Map<String, Object> parametros = new HashMap<>();

        parametros.put(EMPRESA_CUIT.getNombreParametro(), parametrosService.getStringParam(EMPRESA_CUIT));
        parametros.put(EMPRESA_DIRECCION.getNombreParametro(), parametrosService.getStringParam(EMPRESA_DIRECCION));
        parametros.put(EMPRESA_EMAIL.getNombreParametro(), parametrosService.getStringParam(EMPRESA_EMAIL));
        parametros.put(EMPRESA_FECHA_INICIO.getNombreParametro(), parametrosService.getStringParam(EMPRESA_FECHA_INICIO));
        parametros.put(EMPRESA_LOCALIDAD.getNombreParametro(), parametrosService.getStringParam(EMPRESA_LOCALIDAD));
        parametros.put(EMPRESA_NOMBRE.getNombreParametro(), parametrosService.getStringParam(EMPRESA_NOMBRE));
        parametros.put(EMPRESA_NOMBRE_FANTASIA.getNombreParametro(), parametrosService.getStringParam(EMPRESA_NOMBRE_FANTASIA));
        parametros.put(EMPRESA_PROVINCIA.getNombreParametro(), parametrosService.getStringParam(EMPRESA_PROVINCIA));
        parametros.put(EMPRESA_RAZON_SOCIAL.getNombreParametro(), parametrosService.getStringParam(EMPRESA_RAZON_SOCIAL));
        parametros.put(EMPRESA_TELEFONO.getNombreParametro(), parametrosService.getStringParam(EMPRESA_TELEFONO));

        return parametros;
    }
}