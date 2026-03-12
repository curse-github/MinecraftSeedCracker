/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2551
/*    */   extends NamespacedSchema
/*    */ {
/* 25 */   public V2551(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) { super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 32 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, () -> DSL.fields("dimensions", 
/* 33 */           DSL.compoundList(DSL.constType(namespacedString()), DSL.fields("generator", 
/* 34 */               DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", (), "minecraft:noise", ())))))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2551.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */