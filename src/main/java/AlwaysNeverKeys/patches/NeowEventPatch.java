package AlwaysNeverKeys.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

import java.util.ArrayList;

import static AlwaysNeverKeys.AlwaysNeverKeys.PROP_ANK_SETTINGS;
import static AlwaysNeverKeys.AlwaysNeverKeys.alwaysNeverKeysProp;

@SpirePatch(
        clz = NeowEvent.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {boolean.class}
)

public class NeowEventPatch {
  public static void Postfix(NeowEvent __obj_instance, boolean isDone) {
    int keyMode = -1;

    try {
      SpireConfig config = new SpireConfig("alwaysNeverKeys", "alwaysNeverKeysConf", alwaysNeverKeysProp);
      config.load();
      keyMode = config.getInt(PROP_ANK_SETTINGS);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if(keyMode == 1) {

      AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.BLUE));
      AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.GREEN));
      AbstractDungeon.topLevelEffects.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.RED));
      refreshSuperElite();
    } else if(keyMode == 2) {

      Settings.isFinalActAvailable = false;
      refreshSuperElite();
    }
  }

  private static void refreshSuperElite() {
    for(ArrayList<MapRoomNode> nodes : AbstractDungeon.map) {
      for(MapRoomNode node : nodes) {
        if(node.getRoom() instanceof MonsterRoomElite) {
          node.hasEmeraldKey = false;
        }
      }
    }
  }
}
