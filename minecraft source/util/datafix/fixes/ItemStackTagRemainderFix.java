/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public abstract class ItemStackTagRemainderFix
/*    */   extends ItemStackTagFix
/*    */ {
/* 12 */   public ItemStackTagRemainderFix(Schema outputSchema, String name, Predicate<String> idFilter) { super(outputSchema, name, idFilter); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixItemStackTag(Dynamic<T> paramDynamic);
/*    */ 
/*    */   
/* 19 */   protected final Typed<?> fixItemStackTag(Typed<?> tag) { return tag.update(DSL.remainderFinder(), this::fixItemStackTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackTagRemainderFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */