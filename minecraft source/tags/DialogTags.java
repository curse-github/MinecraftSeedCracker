/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DialogTags
/*    */ {
/* 11 */   public static final TagKey<Dialog> PAUSE_SCREEN_ADDITIONS = create("pause_screen_additions");
/* 12 */   public static final TagKey<Dialog> QUICK_ACTIONS = create("quick_actions");
/*    */ 
/*    */   
/* 15 */   private static TagKey<Dialog> create(String name) { return TagKey.create(Registries.DIALOG, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\DialogTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */