/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PlayerHeadBlockProfileFix
/*    */   extends NamedEntityFix
/*    */ {
/* 12 */   public PlayerHeadBlockProfileFix(Schema outputSchema) { super(outputSchema, false, "PlayerHeadBlockProfileFix", References.BLOCK_ENTITY, "minecraft:skull"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fix); }
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> entity) {
/* 21 */     Optional<Dynamic<T>> skullOwner = entity.get("SkullOwner").result();
/* 22 */     Optional<Dynamic<T>> extraType = entity.get("ExtraType").result();
/*    */     
/* 24 */     Optional<Dynamic<T>> profile = skullOwner.or(() -> extraType);
/* 25 */     if (profile.isEmpty()) {
/* 26 */       return entity;
/*    */     }
/* 28 */     entity = entity.remove("SkullOwner").remove("ExtraType");
/* 29 */     return entity.set("profile", ItemStackComponentizationFix.fixProfile((Dynamic)profile.get()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PlayerHeadBlockProfileFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */