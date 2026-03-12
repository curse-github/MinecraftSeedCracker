package net.minecraft.util.debug;

@FunctionalInterface
public interface EventVisitor<T> {
  void accept(T paramT, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugValueAccess$EventVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */