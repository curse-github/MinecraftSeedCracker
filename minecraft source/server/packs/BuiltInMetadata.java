/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public class BuiltInMetadata
/*    */ {
/*  8 */   private static final BuiltInMetadata EMPTY = new BuiltInMetadata(Map.of());
/*    */   
/*    */   private final Map<MetadataSectionType<?>, ?> values;
/*    */ 
/*    */   
/* 13 */   private BuiltInMetadata(Map<MetadataSectionType<?>, ?> values) { this.values = values; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public <T> T get(MetadataSectionType<T> section) { return (T)this.values.get(section); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static BuiltInMetadata of() { return EMPTY; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static <T> BuiltInMetadata of(MetadataSectionType<T> k, T v) { return new BuiltInMetadata(Map.of(k, v)); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static <T1, T2> BuiltInMetadata of(MetadataSectionType<T1> k1, T1 v1, MetadataSectionType<T2> k2, T2 v2) { return new BuiltInMetadata(Map.of(k1, v1, k2, v2)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\BuiltInMetadata.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */