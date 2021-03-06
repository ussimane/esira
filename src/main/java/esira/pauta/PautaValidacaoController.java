/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.pauta;

import ExcelExport.BeanToExcel;
import entidade.DisciplinaEstudante;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matriculaanulada;
import esira.domain.Pauta;
import esira.domain.Users;
import esira.service.CRUDService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Ussimane
 */
public class PautaValidacaoController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winGNotas;
    private Listbox lbpauta;
    private int ano;
    Textbox txx;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "", condrep = " and id.notaFinal >=10 ";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Combobox cbDisc, cbcurso, cbPeriodDisc;
    private Intbox anoi;
    private Button btv;
    private int pos;
    private Menuitem mrep;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        parcond.put("a",cal.get(Calendar.YEAR));
        setLB(0,20);
    }

    public ListModel<Curso> getCursoModel() {
        Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("fac", u.getFaculdade());
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }

    public void onChanging$anoi(InputEvent evt) {
        if (!evt.getValue().equals("")) {
            //check(ahead);
            anoi.setValue(Integer.parseInt(evt.getValue()));
            setLB(0,20);
        }
    }

    public void onSelect$cbPeriodDisc() {
        if (cbPeriodDisc.getSelectedItem() != null) {
        //    check(ahead);
            setLB(0,20);
        }
    }

    public void onSelect$cbcurso() {
        if (cbcurso.getSelectedItem() != null) {
            par.clear();
            par.put("c", (Curso) cbcurso.getSelectedItem().getValue());
            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina o where o.curso = :c", par);
            cbDisc.setModel(new ListModelList<Disciplina>(ld));
            // cbDisc.setSelectedIndex(0);
            check(ahead);
            //setLB(0,20);
        }
    }

    public void onSelect$cbDisc() {
        if (cbDisc.getSelectedItem() != null) {
            check(ahead);
            setLB(0,20);
        }
    }

    public ListModel<Inscricaodisciplina> getListInscDiscModel() {
        parcond.clear();
        parcond.put("a", anoi.getValue().intValue());
        parcond.put("p", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
        parcond.put("d", ((Disciplina) cbDisc.getSelectedItem().getValue()));
        List<Inscricaodisciplina> le = csimpm.findByJPQuery("from Inscricaodisciplina ie where 1=1 and ie.disciplina = :d and "
                + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p", parcond);
        return new ListModelList<Inscricaodisciplina>(le);
    }

    public void onVerInscricao(ForwardEvent evt) {
        Button buton = (Button) evt.getOrigin().getTarget();
        Listitem item = (Listitem) buton.getParent().getParent();
        Estudante e = (Estudante) item.getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        int ind = item.getIndex();
        Listbox l = item.getListbox();
        par.clear();
        par.put("e", e);
        par.put("d", null);
//        List<Inscricaodisciplina> li = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
//                + " and id.disciplinaActiva=3 and id.estado is not null", par);

        if (buton.getImage().equals("/icon/down.png")) {
            buton.setImage("/icon/chevrons.png");
            ListModelList lm = (ListModelList) l.getListModel();
            Object o = null;//lm.getElementAt(++ind);
            int k = 0;
            final Iterator<ListModelList> items = new ArrayList(lm.subList(++ind, lm.getSize())).listIterator();
            while (items.hasNext()) {

                o = items.next();
                if (o != null && !(o instanceof Estudante)) {
                    lm.remove(o);
                    k++;
                } else {
                    break;
                }
                ++ind;
            }
            int r = lbpauta.getRows();
               lbpauta.setRows(r - k);
        } else {
            buton.setImage("/icon/down.png");
            List<DisciplinaEstudante> lde = new ArrayList<DisciplinaEstudante>();
            final Iterator<Disciplina> items2 = new ArrayList(e.getCursocurrente().getDisciplinaList()).listIterator();
            Disciplina d;
            while (items2.hasNext()) {
                d = items2.next();
                DisciplinaEstudante de;
               
                par.replace("d", d);
                List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
                        + " and id.estado is true and id.notaFinal is not null " + condrep + " and id.disciplina = :d order by"
                        + " id.inscricao.dataInscricao asc", par);
                for (Inscricaodisciplina id : lid) {
                    de = new DisciplinaEstudante();
                    de.setDisciplina(d);
                    de.setInscricaodisciplina(id);
                    lde.add(de);
                }
                if(lid.isEmpty()){
                    de = new DisciplinaEstudante();
                     de.setDisciplina(d);
                   de.setInscricaodisciplina(null);
                   lde.add(de);
                }
            }
            int k = ((ListModelList) l.getListModel()).getSize() + 1;
            l.setRows(lde.size() + k);
            for (DisciplinaEstudante de : lde) {
                ++ind;
                de.setId(new Long(e.getIdEstudante()));
                l.setMultiple(false);
               // l.setSizedByContent(false);
             //  l.getListhead().setSizable(false);
                ((ListModelList) l.getListModel()).add(ind, de);
               // l.setSizedByContent(true);
             //   l.getListhead().setSizable(true);
                l.setMultiple(true);
            }
        }
    }

    

    public void onExcelExport() throws ParseException {
        if(lbpauta.getItemCount()==0){
           Clients.showNotification("Sem conte�do","warning", null, null, 3000);
           return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Grade de Notas");
        beanToExcel.exportExcell(lbpauta);
    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onClick$mrep(Event e) throws InterruptedException {
        if (mrep.isChecked()) {
            condrep = " and (id.notaFinal >= 10 or id.notaFinal < 10) ";
        } else {
            condrep = " and id.notaFinal >= 10 ";
        }
        setLB(0,20);
    }

    public void onChanging$txProcNrmec(InputEvent evt) {
            if (!evt.getValue().equals("")&&evt.getValue().charAt(0) != '.') {
                condnr = " and e.nrEstudante = :nr ";
                if (parcond.containsKey("nr")) {
                    parcond.replace("nr", evt.getValue());
                } else {
                    parcond.put("nr", evt.getValue());
                }
            } else {
                condnr = "";
                if (!parcond.containsKey("nr")) return;
                    parcond.remove("nr");
            }
            setLB(0,20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
            if (!evt.getValue().equals("")&&evt.getValue().charAt(0) != '.') {
                condnome = " and lower(e.nomeCompleto) like :nome ";
                if (parcond.containsKey("nome")) {
                    parcond.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
                } else {
                    parcond.put("nome", "%" + evt.getValue().toLowerCase() + "%");
                }
            } else {
                condnome = "";
                if (!parcond.containsKey("nome")) return;
                    parcond.remove("nome");
            }
            setLB(0,20);
    }

    public void onChanging$ibProcAno(InputEvent evt) {
        if (!evt.getValue().equals("")&&evt.getValue().charAt(0) != '.') {
                condano = " and e.anoIngresso = :a ";
                if (parcond.containsKey("a")) {
                    parcond.replace("a", Integer.parseInt(evt.getValue()));
                } else {
                    parcond.put("a", Integer.parseInt(evt.getValue()));
                }
            } else {
                condano = "";
                if (!parcond.containsKey("a"))return;
                    parcond.remove("a");
            }
            setLB(0,20);
    }

//    public void onChange$cbcurso() {
//        // if (cbcurso.getSelectedIndex() != 0) {
//        condcurso = " and e.cursocurrente = :curso ";
//        if (parcond.containsKey("curso")) {
//            parcond.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
//        } else {
//            parcond.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
//        }
//        setLB(0, 20);
//    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbpauta.setModel(new ListModelList<Pauta>());
        }
        parcond.put("a", anoi.getValue());
        List<Estudante> li = csimpm.findByJPQueryFilter("from Pauta e where p.pautaPK.ano = :a and p.valido = 2 order by p.datap desc", parcond, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        lbpauta.setRows(lbpauta.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbpauta.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            pos = pos += li.size();
            btv.setVisible(true);
        }
    }

////    public void onLoadi() {
////        int i = pos;
////        setLB(i, i + 20);
////    }

    private void check(Component component) {
        checkIsValid(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                check(each);
            }
        }
    }

    private void checkParent(Component component) {
        if (component instanceof Tabpanel) {
            ((Tabpanel) component).getTabbox().setSelectedPanel((Tabpanel) component);
        } else {
            checkParent(component.getParent());
        }
    }

    private void checkIsValid(Component component) {
        if (component instanceof InputElement) {
            if ((!((InputElement) component).isValid())) {
                // Force show errorMessage
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((InputElement) component).setFocus(true);
                return;
            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((Combobox) component).open();
                return;
            }
        }
    }
}
