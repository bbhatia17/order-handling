package com.orderhandling.handler;

import com.orderhandling.domains.Orders;
import com.pdflib.PDFlibException;
import com.pdflib.pdflib;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * This is the handler class that contains the packing slip logic
 */
@Component
@RequiredArgsConstructor
public class OrderHandler implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);
    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    /**
     *
     * @param orders
     * @return
     * @throws PDFlibException
     */
    public ResponseEntity packingSlipGenerator(List<Orders> orders){

            logger.info("Enter into method :packingSlipGenerator");
            String outfile = "PackingSlipGeneratorPDF.pdf";
            pdflib p = null;
            int font;
            int tbl = -1;
            int tb2 = -1;
            String optlist;
            final double llx = 50;
            final double lly = 350;
            final double urx = 550;
            final double ury = 650;
            final double llx2 = 50;
            final double lly2 = 550;
            final double urx2 = 550;
            final double ury2 = 500;
            final double llx3 = 50;
            final double lly3 = 450;
            final double urx3 = 550;
            final double ury3 = 50;
            final double llx4 = 50;
            final double lly4 = 350;
            final double urx4 = 550;
            final double ury4 = 50;
            String boldFont = "fillcolor=black fakebold";
            String header = "header=1 rowheightdefault=auto ";
            String fill = "fill={{area=rowodd fillcolor={gray 0.9}}} ";
            String stroke = "stroke={{line=other}} ";
            String fontS = " fontsize=10} ";
            try {
                p = new pdflib();
                if (p.begin_document(outfile, "") == -1)
                    throw new PDFlibException("Document Error: " + p.get_errmsg());
                font = p.load_font("Helvetica", "unicode", "");
                if (font == -1)
                    throw new PDFlibException("Font Error: " + p.get_errmsg());

                /* Start page 1 */
                p.begin_page_ext(0, 0, "width=a4.width height=a4.height");
                p.setfont(font, 12);
                for (Orders order : orders) {
                    p.fit_textline("PACKING SLIP", 300, 750, boldFont);
                    p.fit_textline("Order No.:" + order.getOrderNo(), 50, 700, boldFont);
                    p.fit_textline("Shipping Date:" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", ", "), 300, 700, boldFont);
                    p.fit_textline("Billing Address:" + order.getBillingAddress(), 300, 700, boldFont);
                    p.fit_textline("Shipping Address:" + order.getShippingAddress(), 300, 700, boldFont);

                    optlist = "colwidth=50% fittextline={font=" + font
                            + " fontsize=10}";
                    tbl = p.add_table_cell(tbl, 1, 1, "Tracking Id:" + order.getTrackingId(), optlist);
                    tbl = p.add_table_cell(tbl, 1, 2, "Package Count:" + order.getPackageCount(), optlist);
                    tbl = p.add_table_cell(tbl, 2, 2, "Product Type:" + order.getProductType(), optlist);
                    tbl = p.add_table_cell(tbl, 1, 3, "Shipping Method:" + order.getShippingMethod(), optlist);
                    tbl = p.add_table_cell(tbl, 2, 3, "Membership Type:" + order.getMembershipType(), optlist);
                    tbl = p.add_table_cell(tbl, 1, 4, "MemberShip Status:" + order.getMembershipStatus(), optlist);
                    tbl = p.add_table_cell(tbl, 2, 4, "Payment Status:" + order.getPaymentStatus(), optlist);
                    tbl = p.add_table_cell(tbl, 2, 5, "Total Weight:" + order.getTotalWeight(), optlist);
                    optlist = header
                            + fill
                            + stroke;
                    p.fit_table(tbl, llx, lly, urx, ury, optlist);
                    p.fit_textline("Personal Details", 250, 560, "");
                    optlist = "colwidth=25% fittextline={font=" + font
                            + " fontsize=10}";
                    tb2 = p.add_table_cell(tb2, 1, 1, "Phone NO:" + order.getPhoneNo(), optlist);
                    tb2 = p.add_table_cell(tb2, 2, 1, "Email Address:" + order.getUserEmail(), optlist);
                    optlist = header
                            + fill
                            + stroke;
                    p.fit_table(tb2, llx, lly, urx, ury, optlist);

                    p.fit_textline("Generated Packing Slip:", 50, 275, "");
                    int tf = -1;
                    optlist = "fontname=Helvetica fontsize=12 encoding=unicode ";
                    tf = p.create_textflow(String.valueOf(order), optlist);
                    if (tf == -1)
                        throw new PDFlibException("Error: " + p.get_errmsg());
                    p.fit_textflow(tf, 50, 250, 550, 150,
                            "");
                }
                p.delete_table(tbl, "");
                p.delete_table(tb2, "");
                p.end_page_ext("");
                p.end_document("");
            } catch (PDFlibException e) {
                logger.error("PDFlib exception occurred:");
                logger.error("[" + e.get_errnum() + "] " + e.get_apiname()
                        + ": " + e.get_errmsg());

                throw new RuntimeException(e);

            } catch (Exception e) {
                logger.error("Error in method :packingSlipGenerator {}", e.getMessage());
                throw e;
            } finally {
                if (p != null) {
                    p.delete();
                }
            }
            logger.info("Exit from method :packingSlipGenerator");
            return ResponseEntity.ok().body(p);
    }

}