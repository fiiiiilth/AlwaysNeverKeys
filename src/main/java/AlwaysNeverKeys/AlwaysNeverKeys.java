package AlwaysNeverKeys;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

@SpireInitializer
public class AlwaysNeverKeys implements
        PostInitializeSubscriber,
        EditStringsSubscriber {

  public static Properties alwaysNeverKeysProp = new Properties();
  public static final String PROP_ANK_SETTINGS = "keyMode";

  public static HashMap<String, ArrayList<ModLabeledToggleButton>> keyModeRadioButtons = new HashMap<>();

  public AlwaysNeverKeys() {
    BaseMod.subscribe(this);

    alwaysNeverKeysProp.setProperty(PROP_ANK_SETTINGS, "0");

    try {
      SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
      config.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void initialize() {
    new AlwaysNeverKeys();
  }

  @Override
  public void receivePostInitialize() {
    UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ank:ConfigText");
    String[] STRINGS = uiStrings.TEXT;

    Texture modBadge = new Texture("ankResources/modBadge.png");

    ModPanel settingsPanel = new ModPanel();

    BaseMod.registerModBadge(modBadge, "AlwaysNeverKeys", "fiiiiilth", "", settingsPanel);

    ModLabel keyModeText = new ModLabel(STRINGS[0], 370f, 810f, settingsPanel, (me) -> {});
    settingsPanel.addUIElement(keyModeText);

    keyModeSettingsRadioButton(settingsPanel, 370f, 760f, PROP_ANK_SETTINGS);
  }

  private void keyModeSettingsRadioButton(ModPanel settingsPanel, float x, float y, String keySettings) {
    UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ank:ConfigText");
    String[] STRINGS = uiStrings.TEXT;

    int keyMode = -1;

    try {
      SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
      config.load();
      keyMode = config.getInt(keySettings);
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModLabeledToggleButton radioBtnOff = new ModLabeledToggleButton(STRINGS[1], x, y - 10f, Settings.CREAM_COLOR, FontHelper.charDescFont,
            keyMode == 0, settingsPanel, (label) -> {
    }, (button) -> {
      try {
        SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
        config.setInt(keySettings, 0);
        config.save();
      } catch (Exception e) {
        e.printStackTrace();
      }
      updateButtonStates();
    });
    settingsPanel.addUIElement(radioBtnOff);

    ModLabeledToggleButton radioBtnAlways = new ModLabeledToggleButton(STRINGS[2],
            x, y - 50f, Settings.CREAM_COLOR, FontHelper.charDescFont,
            keyMode == 1, settingsPanel, (label) -> {
    }, (button) -> {
      try {
        SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
        config.setInt(keySettings, 1);
        config.save();
      } catch (Exception e) {
        e.printStackTrace();
      }

      updateButtonStates();
    });
    settingsPanel.addUIElement(radioBtnAlways);

    ModLabeledToggleButton radioBtnNever = new ModLabeledToggleButton(STRINGS[3],
            x, y - 90f, Settings.CREAM_COLOR, FontHelper.charDescFont,
            keyMode == 2, settingsPanel, (label) -> {
    }, (button) -> {
      try {
        SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
        config.setInt(keySettings, 2);
        config.save();
      } catch (Exception e) {
        e.printStackTrace();
      }

      updateButtonStates();
    });
    settingsPanel.addUIElement(radioBtnNever);

    ArrayList<ModLabeledToggleButton> btns = new ArrayList<>();
    btns.add(radioBtnOff);
    btns.add(radioBtnAlways);
    btns.add(radioBtnNever);
    keyModeRadioButtons.put(keySettings, btns);
  }

  public void updateButtonStates() {
    for (String s : keyModeRadioButtons.keySet()) {
      int count = 0;
      int keyMode = -1;

      try {
        SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
        config.load();
        keyMode = config.getInt(s);
      } catch (Exception e) {
        e.printStackTrace();
      }

      for (ModLabeledToggleButton button : keyModeRadioButtons.get(s)) {
        button.toggle.enabled = (count == keyMode);
        count++;
      }
    }
  }

  @Override
  public void receiveEditStrings() {
    String loc = "eng";

    switch (Settings.language) {
      case RUS:
        loc = "rus";
        break;
    }

    loadLangString("eng");
    loadLangString(loc);
  }

  private void loadLangString(String language) {
    String path = "ankResources/localization/" + language + "/UIStrings.json";

    BaseMod.loadCustomStringsFile(UIStrings.class, path);
  }
}
