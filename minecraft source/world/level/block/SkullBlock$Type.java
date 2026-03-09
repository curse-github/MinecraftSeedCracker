/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
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
/*    */ public interface Type
/*    */   extends StringRepresentable
/*    */ {
/* 33 */   public static final Map<String, Type> TYPES = new Object2ObjectArrayMap();
/*    */   static  {
/* 35 */     Objects.requireNonNull(TYPES); CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
/*    */   }
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SkullBlock$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */