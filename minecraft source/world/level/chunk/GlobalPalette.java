/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class GlobalPalette<T>
/*    */   extends Object
/*    */   implements Palette<T> {
/*    */   private final IdMap<T> registry;
/*    */   
/* 12 */   public GlobalPalette(IdMap<T> registry) { this.registry = registry; }
/*    */ 
/*    */ 
/*    */   
/*    */   public int idFor(T value, PaletteResize<T> resizeHandler) {
/* 17 */     int id = this.registry.getId(value);
/*    */     
/* 19 */     return (id == -1) ? 0 : id;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean maybeHas(Predicate<T> predicate) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public T valueFor(int index) {
/* 29 */     T value = (T)this.registry.byId(index);
/* 30 */     if (value == null) {
/* 31 */       throw new MissingPaletteEntryException(index);
/*    */     }
/* 33 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf buffer, IdMap<T> globalMap) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {}
/*    */ 
/*    */ 
/*    */   
/* 46 */   public int getSerializedSize(IdMap<T> globalMap) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public int getSize() { return this.registry.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Palette<T> copy() { return this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\GlobalPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */