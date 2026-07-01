import { ChevronRight } from 'lucide-react';

export default function PipelineTimeline({ steps, currentStep }) {
    return (
        <div className="grid grid-cols-4 gap-4 mb-8">
            {steps.map((step, i) => (
                <div key={step} className="relative">
                    <div className={`p-4 rounded-xl text-center border-b-4 ${i === currentStep ? 'bg-indigo-50 border-indigo-500 text-indigo-700' : 'bg-gray-50 border-gray-200 text-gray-400'}`}>
                        <span className="text-[10px] font-bold uppercase tracking-wider block mb-1">Paso {i + 1}</span>
                        <span className="text-sm font-bold">{step}</span>
                    </div>
                    {i < steps.length - 1 && <ChevronRight className="absolute -right-3 top-1/2 -translate-y-1/2 text-gray-300 w-5 h-5 z-0" />}
                </div>
            ))}
        </div>
    );
}
