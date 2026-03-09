package net.minecraft.server.dialog;

import com.mojang.serialization.MapCodec;
import java.util.List;

public interface SimpleDialog extends Dialog {
  MapCodec<? extends SimpleDialog> codec();
  
  List<ActionButton> mainActions();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\SimpleDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */