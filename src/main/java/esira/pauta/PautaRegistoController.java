/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.pauta;

import ExcelExport.BeanToExcel;
import entidade.DisciplinaEstudante;
import entidade.PautaClassFreq;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matriculaanulada;
import esira.domain.Notapauta;
import esira.domain.Pauta;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.SomaPonderada;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;
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
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Ussimane
 */
public class PautaRegistoController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winPautaReg, winPauta;
    private Listbox lbpauta, lbpautaf, lbpercent, pavaliacao;
    private int ano;
    Textbox txx;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "", condrep = " and id.notaFinal >=10 ";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Combobox cbDisc, cbcurso, cbPeriodDisc, cbdiscip, cbrefpautas;
    private Intbox anoi;
    private Button btv;
    private int pos;
    private Menuitem mrep;
    private Label lbano, lbsem;
    /////
    private Combobox cbtipop;
    private Textbox txnome;
    private Row mpauta, rwpauta;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
//        parcond.put("a",cal.get(Calendar.YEAR));
//        setLB(0,20);
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
            setLB(0, 20);
        }
    }

    public void onSelect$cbPeriodDisc() {
        if (cbPeriodDisc.getSelectedItem() != null) {
            //    check(ahead);
            setLB(0, 20);
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
            setLB(0, 20);
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
                if (lid.isEmpty()) {
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
        if (lbpauta.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
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
        setLB(0, 20);
    }

    public void onChanging$txProcNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and e.nrEstudante = :nr ";
            if (parcond.containsKey("nr")) {
                parcond.replace("nr", evt.getValue());
            } else {
                parcond.put("nr", evt.getValue());
            }
        } else {
            condnr = "";
            if (!parcond.containsKey("nr")) {
                return;
            }
            parcond.remove("nr");
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(e.nomeCompleto) like :nome ";
            if (parcond.containsKey("nome")) {
                parcond.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                parcond.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            condnome = "";
            if (!parcond.containsKey("nome")) {
                return;
            }
            parcond.remove("nome");
        }
        setLB(0, 20);
    }

    public void onChanging$ibProcAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condano = " and e.anoIngresso = :a ";
            if (parcond.containsKey("a")) {
                parcond.replace("a", Integer.parseInt(evt.getValue()));
            } else {
                parcond.put("a", Integer.parseInt(evt.getValue()));
            }
        } else {
            condano = "";
            if (!parcond.containsKey("a")) {
                return;
            }
            parcond.remove("a");
        }
        setLB(0, 20);
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

    ///////////////////////////////////////////////////////////
    public void onSalvaPauta(ForwardEvent evt) {

    }

    public void onCancPauta() {
        winPauta.detach();
    }

    public void onSelecttipop() {
        if (cbdiscip.getSelectedItem() == null) {
            Clients.showNotification("Seleccione uma disciplina", "warning", null, null, 0);
            cbtipop.setSelectedItem(null);
            return;
        }
        int i = Integer.parseInt(cbtipop.getSelectedItem().getValue().toString());
        if (i == 1) {
            rwpauta.getChildren().get(0).detach();
            rwpauta.appendChild((Listbox) mpauta.getChildren().get(0).clone());
            lbpautaf = (Listbox) rwpauta.getChildren().get(0);
            lbpautaf.setId("lbpautaf");
            preenchePauta();
            //
        } else if (i == 2) {
            final Iterator<Listitem> items = lbpercent.getItems().iterator();
            Listitem li;
            while (items.hasNext()) {
                li = items.next();
                Listhead lh = lbpautaf.getListhead();
                Listheader lhr = new Listheader();
                lhr.setLabel(((Pauta) li.getValue()).getDescricao());
                lhr.setHflex("min");
                lhr.setStyle("align:center");
                lhr.setParent(lh);
            }
            //
            Disciplina d = (Disciplina) cbdiscip.getSelectedItem().getValue();
            par.clear();
            par.put("a", Integer.parseInt(lbano.getValue()));
            par.put("p", Short.parseShort(lbsem.getValue().toString()));
            par.put("d", ((Disciplina) cbdiscip.getSelectedItem().getValue()));
            List<Inscricaodisciplina> le = csimpm.findByJPQuery("from Inscricaodisciplina ie where ie.disciplina = :d and "
                    + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p", par);
            ///
            par.clear();
            par.put("a", Integer.parseInt(lbano.getValue()));
            par.put("sem", Short.parseShort(lbsem.getValue()));
            par.put("d", Long.parseLong(lbano.getValue()));
            par.put("e", "");
            par.put("nome", "");
            final Iterator<Inscricaodisciplina> items2 = new ArrayList(le).listIterator();
            Inscricaodisciplina id;
            List<PautaClassFreq> lpf = new ArrayList<PautaClassFreq>();
            PautaClassFreq pf = new PautaClassFreq();
            while (items2.hasNext()) {
                id = ((Inscricaodisciplina) items2.next());
                Estudante e = id.getInscricao().getIdEstudante();
                pf.setId(e.getIdEstudante());
                pf.setEstudante(e);
                pf.setAvaliacao(new ArrayList<Float>());
                //
                final Iterator<Listitem> items3 = lbpercent.getItems().iterator();
                Listitem lip;
                SomaPonderada sp = new SomaPonderada();
                while (items3.hasNext()) {
                    lip = items3.next();
                    par.put("nome", ((Pauta) lip.getValue()).getDescricao());
                    par.put("e", id.getInscricao().getIdEstudante());
                    List<Notapauta> np = csimpm.findEntByJPQuery("from Notapauta np where np.notapautaPK.iddisc = :disc and np.pauta.descricao = :nome"+
                                            " and np.notapautaPK.idestudante = :e and np.notapautaPK.semestre = :sem and np.notapautaPK.ano = :ano"
                            + " and np.pauta.tipo = 0 order by np.notapautaPK.datap desc",par);
                                            if(np!=null&&np.size()>0){
                                               pf.getAvaliacao().add(np.get(0).getNota());
                                               sp.add(np.get(0).getNota(), ((Pauta) lip.getValue()).getPercent());
                                            }else{
                                               pf.getAvaliacao().add(Float.NaN);
                                               sp.add(new Float("0"), ((Pauta) lip.getValue()).getPercent());
                                            }
                }
                pf.setMedia(sp.getMedia());
                lpf.add(pf);
            }
            lbpautaf.setRows(lpf.size());
            lbpautaf.setModel(new ListModelList<PautaClassFreq>(lpf));
        } else if (i == 3) {

        } else if (i == 4) {

        }

    }

    public void onCheckPautaRef(ForwardEvent evt) {
        if (cbrefpautas.isReadonly()) {
            cbrefpautas.setReadonly(true);
            txnome.setReadonly(true);
            cbtipop.setReadonly(true);
            cbtipop.setButtonVisible(false);
        } else {
            cbrefpautas.setReadonly(false);
            txnome.setReadonly(false);
            cbtipop.setReadonly(false);
            cbtipop.setButtonVisible(true);
        }
    }

    public void onSelectdiscip() {
        Disciplina d = (Disciplina) cbdiscip.getSelectedItem().getValue();
        par.clear();
        par.put("a", Integer.parseInt(lbano.getValue()));
        par.put("sem", Short.parseShort(lbsem.getValue()));
        par.put("d", Long.parseLong(lbano.getValue()));
        List<Pauta> lp = csimpm.findByJPQuery("from Pauta p where p.pautaPK.ano = :a and p.pautaPK.semestre = :sem and p.pautaPK.iddisc =:d"
                + "", par);
        List<Pauta> ln = csimpm.findByJPQuery("select pa from Pauta pa where pa.descricao in (select distinct(p.descricao) from Pauta p where p.pautaPK.ano = :a and p.pautaPK.semestre = :sem and p.pautaPK.iddisc =:d"
                + ")order by pa.descricao", par);
        lbpercent.setModel(new ListModelList<Pauta>(lp));

        cbrefpautas.setModel(new ListModelList<Pauta>(lp));
    }

    public void onPautaRef() {
        Pauta p = (Pauta) cbrefpautas.getSelectedItem().getValue();
        if (p.getTipo().intValue() == 1) {
            cbtipop.setSelectedIndex(0);
        } else if (p.getTipo().intValue() == 2) {
            cbtipop.setSelectedIndex(1);
        } else if (p.getTipo().intValue() == 3) {
            cbtipop.setSelectedIndex(2);
        } else if (p.getTipo().intValue() == 4) {
            cbtipop.setSelectedIndex(3);
        }
        onSelecttipop();
    }

    public void preenchePauta() {
        Disciplina d = (Disciplina) cbdiscip.getSelectedItem().getValue();
        par.clear();
        par.put("a", Integer.parseInt(lbano.getValue()));
        par.put("p", Short.parseShort(lbsem.getValue().toString()));
        par.put("d", ((Disciplina) cbdiscip.getSelectedItem().getValue()));
        List<Inscricaodisciplina> le = csimpm.findByJPQuery("from Inscricaodisciplina ie where ie.disciplina = :d and "
                + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p", par);
        lbpautaf.setRows(le.size());
        lbpautaf.setMultiple(false);
        lbpautaf.setModel(new ListModelList<Inscricaodisciplina>(le));
        lbpautaf.setMultiple(true);
    }

    public void onSelectlbpautaf() {
        final Iterator<Listitem> items = lbpautaf.getItems().iterator();
        Listitem li;
        while (items.hasNext()) {
            li = items.next();
            if (li.isSelected()) {
                int siz = li.getChildren().size() - 1;
                ((Doublebox) li.getChildren().get(siz).getChildren().get(0)).setDisabled(false);
                ((Doublebox) li.getChildren().get(siz).getChildren().get(0)).setConstraint(" no Empty: Introduza o Valor!");
            } else {
                int siz = li.getChildren().size() - 1;
                Constraint c = null;
                ((Doublebox) li.getChildren().get(siz).getChildren().get(0)).setConstraint(c);
                ((InputElement) li.getChildren().get(siz).getChildren().get(0)).setText("");
                ((Doublebox) li.getChildren().get(siz).getChildren().get(0)).setDisabled(true);
            }
        }
    }

    public void onActualizarPerc() {
        final Iterator<Listitem> items = lbpercent.getItems().iterator();
        Listitem li;
        par.clear();
        par.put("a", Integer.parseInt(lbano.getValue()));
        par.put("sem", Short.parseShort(lbsem.getValue()));
        par.put("d", Long.parseLong(lbano.getValue()));
        par.put("desc", "");
        while (items.hasNext()) {
            li = items.next();
            int d = ((Intbox) li.getChildren().get(1).getChildren().get(0)).getValue();
            par.replace("desc", ((Pauta) li.getValue()).getDescricao());
            List<Pauta> lp = csimpm.findByJPQuery("from Pauta p where p.pautaPK.ano = :a and p.pautaPK.semestre = :sem and p.pautaPK.iddisc =:d"
                    + " and p.descricao = :desc", par);
            for (Pauta pa : lp) {
                pa.setPercent(d);
                csimpm.update(pa);
            }
        }
    }
    
    public void onCreateDouble(ForwardEvent evt) {
        Doublebox db =  (Doublebox) evt.getOrigin().getTarget();
        db.setDisabled(true);
    }
}
