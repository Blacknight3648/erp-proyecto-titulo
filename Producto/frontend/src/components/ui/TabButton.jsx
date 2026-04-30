export default function TabButton({ children, active, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 rounded-2xl text-[10px] font-black uppercase tracking-widest transition-all ${
        active
          ? "bg-blue-600 text-white shadow-md"
          : "bg-gray-100 text-gray-500 hover:bg-gray-200"
      }`}
    >
      {children}
    </button>
  );
}