export default function TabButton({ children, active, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 rounded-2xl text-[10px] font-black uppercase tracking-widest transition-all ${
        active
          ? "bg-primary text-primary-foreground shadow-md"
          : "bg-secondary text-muted-foreground hover:bg-accent"
      }`}
    >
      {children}
    </button>
  );
}