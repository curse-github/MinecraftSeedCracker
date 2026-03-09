/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Type
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final Codec<Type> CODEC;
/* 42 */   REPLACE_ALL("replace_all", ListOperation.ReplaceAll.MAP_CODEC),
/* 43 */   REPLACE_SECTION("replace_section", ListOperation.ReplaceSection.MAP_CODEC),
/* 44 */   INSERT("insert", ListOperation.Insert.MAP_CODEC),
/* 45 */   APPEND("append", ListOperation.Append.MAP_CODEC);
/*    */   static  {
/* 47 */     CODEC = StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */   private final String id;
/*    */   private final MapCodec<? extends ListOperation> mapCodec;
/*    */   
/*    */   Type(String id, MapCodec<? extends ListOperation> mapCodec) {
/* 53 */     this.id = id;
/* 54 */     this.mapCodec = mapCodec;
/*    */   }
/*    */ 
/*    */   
/* 58 */   public MapCodec<? extends ListOperation> mapCodec() { return this.mapCodec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */