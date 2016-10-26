import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

class BpmControls extends JPanel implements ChangeListener, DocumentListener {

    JSpinner bpmSpinner, swingFactorSpinner;
    JTextField beatCountTextField, basicDurationTextField;
    Metronome metronome;

    JLabel bpmLabel = new JLabel("BPM");
    JLabel timeSignatureLabel = new JLabel("Time Signature");
    JLabel swingFactorLabel = new JLabel("Groove (%):");
    JLabel slash = new JLabel("/");

    BpmControls(Metronome metronome) {
        this.metronome = metronome;
        this.setSize(100, 100);

        beatCountTextField = new JTextField(Integer.toString(metronome.GetBeatCount()));
        basicDurationTextField = new JTextField(Integer.toString(metronome.GetBasicDuration()));
        bpmSpinner = new JSpinner(new SpinnerNumberModel(metronome.GetBeatsPerMinute(), 1, 240, 1));
        swingFactorSpinner = new JSpinner(new SpinnerNumberModel((int) (metronome.GetSwingFactor() * 100), 1, 100, 1));

        beatCountTextField.getDocument().addDocumentListener(this);
        basicDurationTextField.getDocument().addDocumentListener(this);
        bpmSpinner.addChangeListener(this);
        swingFactorSpinner.addChangeListener(this);

        this.add(bpmLabel);
        this.add(bpmSpinner);
        this.add(swingFactorLabel);
        this.add(swingFactorSpinner);
        this.add(timeSignatureLabel);
        this.add(beatCountTextField);
        this.add(slash);
        this.add(basicDurationTextField);
    }

    public void BpmAdd(int numberToAdd) {
        bpmSpinner.setValue((int) bpmSpinner.getValue() + numberToAdd);
    }

    public void UpdateMetronome() {
        this.metronome.Update(
            Integer.parseInt(beatCountTextField.getText()),
            Integer.parseInt(basicDurationTextField.getText()),
            (int) bpmSpinner.getValue(),
            ((Integer) swingFactorSpinner.getValue()).doubleValue() / 100.0
        );
    }

    public void stateChanged(ChangeEvent e) {
        this.UpdateMetronome();
    }

    public void changedUpdate(DocumentEvent e) {
        this.UpdateMetronome();
    }

    public void removeUpdate(DocumentEvent e) {
        this.UpdateMetronome();
    }

    public void insertUpdate(DocumentEvent e) {
        this.UpdateMetronome();
    }
}
