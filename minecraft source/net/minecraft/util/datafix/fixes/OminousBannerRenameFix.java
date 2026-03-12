/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OminousBannerRenameFix
/*    */   extends ItemStackTagFix
/*    */ {
/* 15 */   public OminousBannerRenameFix(Schema outputSchema) { super(outputSchema, "OminousBannerRenameFix", id -> id.equals("minecraft:white_banner")); }
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fixItemStackTag(Dynamic<T> tag) {
/* 19 */     return tag.update("display", display -> display.update("Name", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected Typed<?> fixItemStackTag(Typed<?> tag) { return Util.writeAndReadTypedOrThrow(tag, tag.getType(), this::fixItemStackTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */