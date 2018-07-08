/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import ExcelExport.BeanToExcel;
import esira.domain.Areacientifica;
import esira.domain.Caracter;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Lecciona;
import esira.domain.LeccionaPK;
import esira.domain.Precedencia;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.service.CRUDService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author user
 */
public class DocenteDiscController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;

    private Window windowDocDisc, winDocDiscDialog;
    private Combobox cbfunc;
    //private Textarea txtareaObjGeral;
    private Combobox cbCursoPrec, cbdiscipPre, cbSemPrec, cbTurno;
    private Listbox lbDiscip, lbdocdisc;
    private String ord = " order by d.nome";
    private String sql = "";
    // Doublebox d;
    Checkbox ch;
    Row rw;
    String cond;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso;
    private Intbox ibturma;
    Menuitem manoi;
    private Intbox poscf;
    private String pesqf = null, condnf = "";
    Map<String, Object> cbparf = new HashMap<String, Object>();
    private int indcf = -1;
    private Listbox lbdocente;
    private Intbox iddisc, ibitem;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (poscf != null) {
            poscf.setValue(0);
        }
    }

    public ListModel<Funcionario> getFuncionarioModel() {
        List<Funcionario> lf = csimp.getAll(Funcionario.class);
        return new ListModelList<Funcionario>(lf);
    }

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> faculdades = csimp.getAll(Faculdade.class);
        return new ListModelList<Faculdade>(faculdades);
    }

    //............................................................................................................
    public ListModel<Curso> getCursoModel() {
        // return cursoModel;
        Map<String, Object> par = new HashMap<>();
        par.put("idf", usr.getUtilizador());
        Faculdade f = ((Users) csimp.findByJPQuery("from Users u where u.utilizador = :idf", par).get(0)).getFaculdade();
        return new ListModelList<Curso>(f.getCursoList());
    }

    //............................................................................
    public ListModel<Disciplina> getDiscipModel() {
        //return disciplinaModel;
        Map<String, Object> par = new HashMap<>();
        par.put("idf", usr.getUtilizador());
        Faculdade f = ((Users) csimp.findByJPQuery("from Users u where u.utilizador = :idf", par).get(0)).getFaculdade();
        par.clear();
        par.put("idf", f);
        List<Disciplina> disc = csimp.findByJPQuery("from Disciplina d where d.curso.faculdade = :idf", par);
        return new ListModelList<Disciplina>(disc);
    }

    //....................................................................................................................................................................................
    public void onChange$cbCursoPrec(Event event) {
        selCursoPrec();
    }

    public void onChange$cbSemPrec(Event envent) {
        selCursoPrec();
    }

    public void selCursoPrec() {
        if (cbCursoPrec.getSelectedItem() == null) {
            return;
        }
        Curso c = (Curso) cbCursoPrec.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        par.put("cu", c);
        // ent.put("d", Disciplina.class);
        String sql = "from Disciplina d where d.curso = :cu";
        if (cbSemPrec.getSelectedItem() != null) {
            int s = Integer.parseInt(cbSemPrec.getSelectedItem().getValue().toString());
            if (s != 0) {
                par.put("s", s);
                sql = sql + " and d.semestre = :s";
            }
        }
        List<Disciplina> ldisc = csimp.findByJPQuery(sql, par);
        lbdocdisc.setModel(new ListModelList<Disciplina>());
        lbdocdisc.setRows(lbdocdisc.getItemCount() + ldisc.size()+1);
        final Iterator<Disciplina> items = ldisc.iterator();
        Disciplina e;
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbdocdisc.getModel()).add(e);
        }
    }

    public void onEditDocDisc(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        d = csimp.load(Disciplina.class, d.getIdDisc());
        Window w = (Window) Executions.createComponents("/planoc/formServicoDoc.zul", windowDocDisc, null);
        ((Intbox) w.getFellow("iddisc")).setValue(d.getIdDisc().intValue());
        ((Intbox) w.getFellow("ibitem")).setValue(litem.getIndex());
        ((Textbox) w.getFellow("lbNome")).setValue(d.getNome());
        Listbox docs = ((Listbox) w.getFellow("lbdocente"));
        List<Lecciona> li = d.getLeccionaList();
        int sl = li.size();
        if (sl > 0) {
            docs.setRows(sl + 1);
        }
        docs.setModel(new ListModelList<Lecciona>(li));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ListModel<Curso> getListaCursoModel() {
        Users u = csimp.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("fac", u.getFaculdade());
        List<Curso> lc = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }

    public void onExcelExportLista() throws ParseException {
        if (lbdocdisc.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Distribuicao de Serviço Docente - " + ((Curso) cbCursoPrec.getSelectedItem().getValue()).getDescricao()+" "+cbSemPrec.getSelectedItem().getLabel());
        beanToExcel.exportExcell(lbdocdisc);
    }

    public void onSCbfunc() {
        if (cbfunc.getSelectedIndex() == poscf.getValue() - 1) {
            onLoadCombf();
            cbfunc.open();
            cbfunc.setText(pesqf);
            return;
        }
        indcf = cbfunc.getSelectedIndex();
    }

    public void onCbfunc(InputEvent evt) {
        indcf = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesqf = evt.getValue();
            condnf = " and lower(f.nome) like :nome ";
            if (cbparf.containsKey("nome")) {
                cbparf.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                cbparf.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesqf != null) {
                pesqf = "";
            }
            condnf = "";
            if (!cbparf.containsKey("nome")) {
                return;
            }
            cbparf.remove("nome");
        }
        cbfunc.getItems().clear();
        setLBCombf(0, 20);
    }

    public void onOpen$cbfunc() {
//        if (poscf.getValue() == 0) {
//            cbfunc.getItems().clear();
//            setLBCombf(0, 20);
//        }
        if (pesqf == null) {
            setLBCombf(0, 20);
            pesqf = "";
        } else {
            if (cbfunc.isOpen()) {
                cbfunc.setText(pesqf);
            } else {
                if (indcf != -1) {
                    cbfunc.getSelectedIndex();
                    cbfunc.setSelectedIndex(indcf);
                }
            }
        }
    }

    public void setLBCombf(int i, int j) {
        if (j == 20) {
            cbfunc.setModel(new ListModelList<Funcionario>());
        }
        List<Funcionario> li = null;
        Users u = csimp.get(Users.class, usr.getUtilizador());
        // par.clear();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        li = csimp.findByJPQueryFilter("select d.funcionario from Docente d " + condnf + " order by d.funcionario.nome", cbparf, i, j);
        final Iterator<Funcionario> items = li.iterator();
        Funcionario e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) cbfunc.getModel()).remove(new Funcionario());
            new Combobox().appendChild(cbfunc.getItemAtIndex(poscf.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) cbfunc.getModel()).add(e);
        }
        if (li.size() == j) {
            Funcionario es = new Funcionario();
            es.setNome("-------Ver Mais-------");
            ((ListModelList) cbfunc.getModel()).add(es);
            poscf.setValue(((ListModelList) cbfunc.getModel()).size());
        }
    }

    public void onLoadCombf() {
        int i = ((ListModelList) cbfunc.getModel()).size() - 1;
        setLBCombf(i, i + 20);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    public void onBtnva() {
        if (cbfunc.getSelectedItem() == null) {
            return;
        }
        Funcionario d = (Funcionario) cbfunc.getSelectedItem().getValue();
        Lecciona le = new Lecciona(d.getDocente().getIddocente(),iddisc.getValue().longValue()
                ,Integer.parseInt(cbTurno.getSelectedItem().getValue().toString()), ibturma.getValue());
        le.setDocente(d.getDocente());
        if (!((ListModelList) lbdocente.getListModel()).contains(le)) {
            ((ListModelList) lbdocente.getListModel()).add(le);
        }
    }
    
//    public ListModel getFuncModel(){
//       List<Docente> lf = csimp.getAll(Docente.class);
//       return new ListModelList<Docente>(lf);
//    }

    public void onBtnRegistar() {
        Disciplina d = csimp.get(Disciplina.class, iddisc.getValue().longValue());
        Iterator<Lecciona> items = new ArrayList(d.getLeccionaList()).listIterator();
        Lecciona l;
        while (items.hasNext()) {
            l = items.next();
            d.getLeccionaList().remove(l);
            csimp.delete(csimp.get(Lecciona.class, l.getLeccionaPK()));
        }
        Lecciona le;
        Iterator<Listitem> items1 = lbdocente.getItems().iterator();
        while (items1.hasNext()) {
            le = (Lecciona) items1.next().getValue();
            le.setDisciplina(d);
            //le.setDocente(null);
            d.getLeccionaList().add(le);
        }
        csimp.update(d);
        Clients.showNotification(" Actualizado com sucesso", null, null, null, 2000);
        ((ListModelList) lbdocdisc.getModel()).set(ibitem.getValue(), d);
        winDocDiscDialog.detach();
    }

    public void onCancAnul() {
        winDocDiscDialog.detach();
    }
}
