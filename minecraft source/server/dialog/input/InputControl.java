/*   */ package net.minecraft.server.dialog.input;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ 
/*   */ public interface InputControl
/*   */ {
/* 8 */   public static final MapCodec<InputControl> MAP_CODEC = BuiltInRegistries.INPUT_CONTROL_TYPE.byNameCodec().dispatchMap(InputControl::mapCodec, c -> c);
/*   */   
/*   */   MapCodec<? extends InputControl> mapCodec();
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\InputControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */