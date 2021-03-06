package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.domain.AFIPAuthServices;
import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;
import ar.com.gtsoftware.domain.FiscalLibroIvaVentasLineas;
import ar.com.gtsoftware.dto.fiscal.CAEResponse;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.fe.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
public class ElectronicInvoiceClient extends WebServiceGatewaySupport {

    private static final String CAE_SOLICITAR_SOAP_ACTION = "http://ar.gov.afip.dif.FEV1/FECAESolicitar";
    private static final String ULTIMO_AUTORIZADO_SOAP_ACTION = "http://ar.gov.afip.dif.FEV1/FECompUltimoAutorizado";
    private static final String SUCCESS_RESULT = "A";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ParametrosService parametrosService;

    public int getLastAuthorizedInvoiceNumber(AFIPAuthServices authToken, int pointOfSale, int invoiceType) {

        FECompUltimoAutorizado lastAuthorizedRequest = new FECompUltimoAutorizado();
        lastAuthorizedRequest.setCbteTipo(invoiceType);
        lastAuthorizedRequest.setPtoVta(pointOfSale);
        lastAuthorizedRequest.setAuth(transformAuth(authToken));

        final Object soapResponse = getWebServiceTemplate().marshalSendAndReceive(lastAuthorizedRequest,
                message -> ((SoapMessage) message).setSoapAction(ULTIMO_AUTORIZADO_SOAP_ACTION));

        FECompUltimoAutorizadoResponse lastInvoice = (FECompUltimoAutorizadoResponse) soapResponse;

        return lastInvoice.getFECompUltimoAutorizadoResult().getCbteNro();
    }

    private FEAuthRequest transformAuth(AFIPAuthServices authToken) {
        FEAuthRequest authRequest = new FEAuthRequest();
        authRequest.setToken(authToken.getToken());
        authRequest.setSign(authToken.getSign());
        authRequest.setCuit(parametrosService.getLongParam(Parametros.EMPRESA_CUIT));

        return authRequest;
    }

    public CAEResponse requestElectronicAuthorization(AFIPAuthServices authToken, FiscalLibroIvaVentas registro) {
        FECAESolicitar solicitarRequest = new FECAESolicitar();
        solicitarRequest.setAuth(transformAuth(authToken));

        FECAERequest caeRequest = transformCaeRequest(registro);

        solicitarRequest.setFeCAEReq(caeRequest);

        final Object soapResponse = getWebServiceTemplate().marshalSendAndReceive(solicitarRequest,
                message -> ((SoapMessage) message).setSoapAction(CAE_SOLICITAR_SOAP_ACTION));

        FECAESolicitarResponse caeReponse = (FECAESolicitarResponse) soapResponse;
        final FECAECabResponse cabResp = caeReponse.getFECAESolicitarResult().getFeCabResp();

        if (cabResp != null && StringUtils.equals(SUCCESS_RESULT, cabResp.getResultado())) {
            final FECAEDetResponse fecaeDetResponse = caeReponse.getFECAESolicitarResult().getFeDetResp()
                    .getFECAEDetResponse().get(0);

            final long cae = Long.parseLong(fecaeDetResponse.getCAE());
            final LocalDate fechaVencimientoCae = LocalDate.parse(fecaeDetResponse.getCAEFchVto(), DATE_TIME_FORMATTER);

            return new CAEResponse(cae, fechaVencimientoCae);
        }

        throw new RuntimeException(caeReponse.getFECAESolicitarResult().getErrors().getErr().toString());
    }

    private FECAERequest transformCaeRequest(FiscalLibroIvaVentas registro) {
        FECAERequest caeRequest = new FECAERequest();

        final FECAECabRequest cabRequest = new FECAECabRequest();
        cabRequest.setCantReg(1);
        cabRequest.setCbteTipo(parseInt(registro.getCodigoTipoComprobante().getCodigoTipoComprobante()));
        cabRequest.setPtoVta(parseInt(registro.getPuntoVentaFactura()));

        caeRequest.setFeCabReq(cabRequest);

        final ArrayOfFECAEDetRequest arrayDetRequest = new ArrayOfFECAEDetRequest();
        final FECAEDetRequest detRequest = new FECAEDetRequest();

        detRequest.setConcepto(3);//Hardcoded
        detRequest.setDocTipo(registro.getIdPersona().getIdTipoDocumento().getFiscalCodigoTipoDocumento());
        detRequest.setDocNro(Long.parseLong(registro.getDocumento()));
        detRequest.setCbteDesde(Long.parseLong(registro.getNumeroFactura()));
        detRequest.setCbteHasta(Long.parseLong(registro.getNumeroFactura()));
        final String formattedInvoiceDate = DATE_TIME_FORMATTER.format(registro.getFechaFactura());
        detRequest.setCbteFch(formattedInvoiceDate);
        detRequest.setImpTotal(registro.getTotalFactura().abs().doubleValue());
        detRequest.setImpTotConc(0);

        final ArrayOfAlicIva arrayOfAlicIva = new ArrayOfAlicIva();
        for (FiscalLibroIvaVentasLineas lineaIva : registro.getFiscalLibroIvaVentasLineasList()) {
            AlicIva alicIva = new AlicIva();
            alicIva.setId(lineaIva.getIdAlicuotaIva().getFiscalCodigoAlicuota());
            alicIva.setBaseImp(lineaIva.getNetoGravado().abs().doubleValue());
            alicIva.setImporte(lineaIva.getImporteIva().abs().doubleValue());

            arrayOfAlicIva.getAlicIva().add(alicIva);
        }
        detRequest.setIva(arrayOfAlicIva);


        detRequest.setImpNeto(registro.getImporteNetoGravado().abs().doubleValue());
        detRequest.setImpOpEx(registro.getImporteExento().abs().doubleValue());
        detRequest.setImpIVA(registro.getImporteIva().abs().doubleValue());
        detRequest.setImpTrib(registro.getImporteTributos().abs().doubleValue());

        detRequest.setFchServDesde(formattedInvoiceDate);
        detRequest.setFchServHasta(formattedInvoiceDate);
        detRequest.setFchVtoPago(formattedInvoiceDate);

        detRequest.setMonId("PES");
        detRequest.setMonCotiz(1);

        arrayDetRequest.getFECAEDetRequest().add(detRequest);
        caeRequest.setFeDetReq(arrayDetRequest);

        return caeRequest;
    }

    protected ParametrosService getParametrosService() {
        return parametrosService;
    }
}
