/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class DialogTypes {
/*    */   public static MapCodec<? extends Dialog> bootstrap(Registry<MapCodec<? extends Dialog>> registry) {
/*  8 */     Registry.register(registry, "notice", NoticeDialog.MAP_CODEC);
/*  9 */     Registry.register(registry, "server_links", ServerLinksDialog.MAP_CODEC);
/* 10 */     Registry.register(registry, "dialog_list", DialogListDialog.MAP_CODEC);
/* 11 */     Registry.register(registry, "multi_action", MultiActionDialog.MAP_CODEC);
/* 12 */     return (MapCodec)Registry.register(registry, "confirmation", ConfirmationDialog.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\DialogTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */