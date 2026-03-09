/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ActionTypes {
/*    */   public static MapCodec<? extends Action> bootstrap(Registry<MapCodec<? extends Action>> registry) {
/*  9 */     StaticAction.WRAPPED_CODECS.forEach((action, codec) -> 
/* 10 */         Registry.register(registry, Identifier.withDefaultNamespace(action.getSerializedName()), codec));
/*    */     
/* 12 */     Registry.register(registry, Identifier.withDefaultNamespace("dynamic/run_command"), CommandTemplate.MAP_CODEC);
/* 13 */     return (MapCodec)Registry.register(registry, Identifier.withDefaultNamespace("dynamic/custom"), CustomAll.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\ActionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */