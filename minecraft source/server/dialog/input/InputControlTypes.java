/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class InputControlTypes {
/*    */   public static MapCodec<? extends InputControl> bootstrap(Registry<MapCodec<? extends InputControl>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("boolean"), BooleanInput.MAP_CODEC);
/* 10 */     Registry.register(registry, Identifier.withDefaultNamespace("number_range"), NumberRangeInput.MAP_CODEC);
/* 11 */     Registry.register(registry, Identifier.withDefaultNamespace("single_option"), SingleOptionInput.MAP_CODEC);
/* 12 */     return (MapCodec)Registry.register(registry, Identifier.withDefaultNamespace("text"), TextInput.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\InputControlTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */