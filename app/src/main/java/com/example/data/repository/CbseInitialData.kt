package com.example.data.repository

import com.example.data.model.*

object CbseInitialData {

    val chapters: List<ChapterEntity> = listOf(
        // MATHEMATICS
        ChapterEntity(
            id = "math_ch1",
            subjectCode = "MATH",
            chapterNumber = 1,
            title = "Relations and Functions",
            cbseWeightageMarks = 6,
            summaryNotes = "• Types of Relations: Empty, Universal, Reflexive ((a,a)∈R), Symmetric ((a,b)∈R ⇒ (b,a)∈R), and Transitive ((a,b)∈R, (b,c)∈R ⇒ (a,c)∈R).\n• Equivalence Relation: Relation which is reflexive, symmetric and transitive simultaneously.\n• Equivalence Classes: Partition set into disjoint subsets [a] = {x ∈ A : (x,a) ∈ R}.\n• Types of Functions: One-one (Injective: f(x1)=f(x2) ⇒ x1=x2), Onto (Surjective: Range = Codomain), Bijective (Both Injective and Surjective).\n• Invertible Functions: A function f: X → Y is invertible if and only if f is bijective.",
            keyFormulas = "• Reflexive: ∀ a ∈ A, (a,a) ∈ R\n• Symmetric: (a,b) ∈ R ⇒ (b,a) ∈ R\n• Transitive: (a,b) ∈ R & (b,c) ∈ R ⇒ (a,c) ∈ R\n• Injective: f(x₁) = f(x₂) ⇔ x₁ = x₂\n• Number of Relations on set with n elements = 2^(n²)",
            totalQuestionsCount = 15,
            completedQuestionsCount = 6,
            masteryPercentage = 68,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "math_ch2",
            subjectCode = "MATH",
            chapterNumber = 2,
            title = "Inverse Trigonometric Functions",
            cbseWeightageMarks = 4,
            summaryNotes = "• Principal Value Branches:\n  - sin⁻¹x: Domain [-1, 1], Range [-π/2, π/2]\n  - cos⁻¹x: Domain [-1, 1], Range [0, π]\n  - tan⁻¹x: Domain ℝ, Range (-π/2, π/2)\n  - cot⁻¹x: Domain ℝ, Range (0, π)\n  - sec⁻¹x: Domain ℝ-(-1,1), Range [0, π] - {π/2}\n  - cosec⁻¹x: Domain ℝ-(-1,1), Range [-π/2, π/2] - {0}\n• Negative angle identities:\n  - sin⁻¹(-x) = -sin⁻¹(x)\n  - cos⁻¹(-x) = π - cos⁻¹(x)\n  - tan⁻¹(-x) = -tan⁻¹(x)\n  - sec⁻¹(-x) = π - sec⁻¹(x)",
            keyFormulas = "• sin⁻¹(x) + cos⁻¹(x) = π/2 (x ∈ [-1, 1])\n• tan⁻¹(x) + cot⁻¹(x) = π/2 (x ∈ ℝ)\n• cosec⁻¹(x) + sec⁻¹(x) = π/2 (|x| ≥ 1)\n• tan⁻¹x + tan⁻¹y = tan⁻¹((x+y)/(1-xy)) for xy < 1",
            totalQuestionsCount = 12,
            completedQuestionsCount = 10,
            masteryPercentage = 85,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "math_ch3",
            subjectCode = "MATH",
            chapterNumber = 3,
            title = "Matrices & Determinants",
            cbseWeightageMarks = 10,
            summaryNotes = "• Matrices: Row, Column, Square, Diagonal, Scalar, Identity, Zero matrices.\n• Symmetric (A' = A) and Skew-Symmetric (A' = -A) matrices. Any square matrix = (1/2)(A+A') + (1/2)(A-A').\n• Determinant: Area of triangle = (1/2) |x₁(y₂-y₃) + x₂(y₃-y₁) + x₃(y₁-y₂)|.\n• Invertible Matrix: A⁻¹ = (1/|A|) adj(A), exists iff |A| ≠ 0 (Non-singular matrix).\n• System of Linear Equations: AX = B ⇒ X = A⁻¹B if |A| ≠ 0.",
            keyFormulas = "• |A'| = |A|\n• |AB| = |A|·|B|\n• |kA| = kⁿ|A| (for n×n matrix)\n• A·adj(A) = adj(A)·A = |A|·I\n• |adj(A)| = |A|^(n-1)\n• A⁻¹ = (adj A) / |A|",
            totalQuestionsCount = 20,
            completedQuestionsCount = 18,
            masteryPercentage = 92,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "math_ch4",
            subjectCode = "MATH",
            chapterNumber = 4,
            title = "Integrals & Differential Equations",
            cbseWeightageMarks = 15,
            summaryNotes = "• Standard Integrals & Integration by Parts: ∫ u v dx = u ∫ v dx - ∫ (u' ∫ v dx) dx (ILATE rule).\n• Special Integrals: ∫ dx/(x²+a²), ∫ dx/√(a²-x²), ∫ √(a²-x²) dx.\n• Definite Integral Properties: P0 to P7, King's Property: ∫[a,b] f(x) dx = ∫[a,b] f(a+b-x) dx.\n• Differential Equations: Order (highest derivative) vs Degree (power of highest derivative).\n• Linear Differential Equation: dy/dx + Py = Q ⇒ IF = e^(∫P dx), solution: y·(IF) = ∫(Q·IF) dx + C.",
            keyFormulas = "• ∫ e^x [f(x) + f'(x)] dx = e^x f(x) + C\n• ∫ 1/(x²+a²) dx = (1/a) tan⁻¹(x/a) + C\n• ∫ 1/√(a²-x²) dx = sin⁻¹(x/a) + C\n• Linear D.E: IF = e^(∫ P dx) → y·IF = ∫ (Q·IF) dx + C",
            totalQuestionsCount = 25,
            completedQuestionsCount = 8,
            masteryPercentage = 48,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "math_ch5",
            subjectCode = "MATH",
            chapterNumber = 5,
            title = "Vector Algebra & 3D Geometry",
            cbseWeightageMarks = 14,
            summaryNotes = "• Vectors: Dot product a·b = |a||b|cosθ. Cross product a×b = |a||b|sinθ n̂.\n• Direction Cosines (l,m,n): l²+m²+n² = 1. Direction ratios (a,b,c).\n• Equation of Line: r = a + λb, Cartesian: (x-x₁)/a = (y-y₁)/b = (z-z₁)/c.\n• Shortest distance between skew lines: d = |(b₁×b₂)·(a₂-a₁)| / |b₁×b₂|.\n• Parallel lines distance: d = |b × (a₂-a₁)| / |b|.",
            keyFormulas = "• a·b = a₁b₁ + a₂b₂ + a₃b₃\n• cosθ = (a·b) / (|a||b|)\n• Distance between skew lines: d = |(a₂-a₁)·(b₁×b₂)| / |b₁×b₂|\n• Angle between lines: cosθ = |a₁a₂+b₁b₂+c₁c₂| / [√(a₁²+b₁²+c₁²) √(a₂²+b₂²+c₂²)]",
            totalQuestionsCount = 18,
            completedQuestionsCount = 14,
            masteryPercentage = 82,
            isOfflineReady = true
        ),

        // PHYSICS
        ChapterEntity(
            id = "phys_ch1",
            subjectCode = "PHYS",
            chapterNumber = 1,
            title = "Electric Charges and Fields",
            cbseWeightageMarks = 8,
            summaryNotes = "• Coulomb's Law: F = (1/4πε₀) · (|q₁q₂|/r²), ε₀ = 8.854 × 10⁻¹² C²N⁻¹m⁻².\n• Electric Field on Axis of Dipole: E_axial = (2kp) / r³ (for r >> a).\n• Electric Field on Equator of Dipole: E_equatorial = -(kp) / r³.\n• Torque on Dipole in Uniform Field: τ = p × E.\n• Gauss's Law: ∮ E·dA = q_enclosed / ε₀.\n• Applications of Gauss's Law:\n  1. Infinitely long wire: E = λ / (2πε₀r)\n  2. Infinite plane sheet: E = σ / (2ε₀)\n  3. Thin spherical shell: E_inside = 0, E_outside = kq/r².",
            keyFormulas = "• F = (1/4πε₀)(q₁q₂/r²)\n• E_axial = 2kp/r³, E_eq = kp/r³\n• Torque τ = p × E = pE sinθ\n• Gauss's Law: Φ = ∮ E·dA = Q/ε₀\n• Line Charge: E = λ/(2πε₀r)\n• Infinite Sheet: E = σ/(2ε₀)",
            totalQuestionsCount = 20,
            completedQuestionsCount = 16,
            masteryPercentage = 88,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "phys_ch2",
            subjectCode = "PHYS",
            chapterNumber = 2,
            title = "Current Electricity",
            cbseWeightageMarks = 7,
            summaryNotes = "• Drift Velocity: v_d = -(eEτ / m), Current I = n·A·e·v_d.\n• Ohm's Law in vector form: J = σE (where σ = 1/ρ = conductivity).\n• Temperature dependence: ρ_T = ρ₀(1 + α(T - T₀)).\n• Kirchhoff's Rules: 1st Law (KCL - Charge conservation), 2nd Law (KVL - Energy conservation).\n• Wheatstone Bridge condition: P/Q = R/S when galvanometer shows null deflection.",
            keyFormulas = "• I = n A e v_d\n• v_d = (e E τ) / m\n• Resistance R = ρ L / A\n• Internal resistance: r = R (E/V - 1)\n• Wheatstone Bridge balance: P/Q = R/S\n• Electric Power P = V I = I²R = V²/R",
            totalQuestionsCount = 18,
            completedQuestionsCount = 12,
            masteryPercentage = 72,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "phys_ch3",
            subjectCode = "PHYS",
            chapterNumber = 3,
            title = "Ray Optics and Optical Instruments",
            cbseWeightageMarks = 9,
            summaryNotes = "• Lens Maker's Formula: 1/f = (μ - 1) [1/R₁ - 1/R₂].\n• Refraction at spherical surface: (μ₂/v) - (μ₁/u) = (μ₂ - μ₁) / R.\n• Prism formula: μ = sin((A + δ_m)/2) / sin(A/2).\n• Compound Microscope Magnification: M = -(L/f_o) · (1 + D/f_e).\n• Astronomical Telescope: Magnification m = -f_o / f_e, Tube length L = f_o + f_e in normal adjustment.",
            keyFormulas = "• Lens Formula: 1/v - 1/u = 1/f\n• Lens Maker's: 1/f = (μ-1)(1/R₁ - 1/R₂)\n• Power of Combination: P = P₁ + P₂ ⇒ 1/F = 1/f₁ + 1/f₂\n• Prism: μ = sin((A+δ_m)/2) / sin(A/2)\n• Microscope: m = (v_o/u_o)(1 + D/f_e)\n• Telescope: m = f_o / f_e (Normal adj.)",
            totalQuestionsCount = 22,
            completedQuestionsCount = 9,
            masteryPercentage = 54,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "phys_ch4",
            subjectCode = "PHYS",
            chapterNumber = 4,
            title = "Electromagnetic Induction & AC",
            cbseWeightageMarks = 9,
            summaryNotes = "• Faraday's Law: ε = -dΦ_B / dt. Lenz's Law indicates direction according to conservation of energy.\n• Motional EMF: ε = B l v. Self Inductance: L = μ₀ n² A l.\n• AC Circuits: RMS values: I_rms = I₀/√2 = 0.707 I₀, V_rms = V₀/√2.\n• Series LCR Circuit: Impedance Z = √[R² + (X_L - X_C)²], tanφ = (X_L - X_C)/R.\n• Resonance condition: X_L = X_C ⇒ ω₀ = 1/√(LC), Quality factor Q = (1/R) √(L/C).",
            keyFormulas = "• ε = -N (dΦ/dt)\n• Motional EMF: ε = Bvl\n• LCR Impedance: Z = √[R² + (ωL - 1/ωC)²]\n• Resonance: f₀ = 1 / (2π√(LC))\n• Power Factor: cosφ = R/Z\n• Transformer: V_s / V_p = N_s / N_p = I_p / I_s",
            totalQuestionsCount = 20,
            completedQuestionsCount = 15,
            masteryPercentage = 80,
            isOfflineReady = true
        ),

        // CHEMISTRY
        ChapterEntity(
            id = "chem_ch1",
            subjectCode = "CHEM",
            chapterNumber = 1,
            title = "Solutions",
            cbseWeightageMarks = 7,
            summaryNotes = "• Raoult's Law: P_A = P_A° · x_A. For ideal solutions: ΔH_mix = 0, ΔV_mix = 0.\n• Positive deviation (A-B weaker than A-A/B-B, e.g., Ethanol + Acetone) vs Negative deviation (A-B stronger, e.g., Chloroform + Acetone).\n• Colligative Properties (depend only on number of solute particles):\n  1. Relative Lowering of Vapour Pressure: (P° - P)/P° = i · x₂\n  2. Elevation of Boiling Point: ΔT_b = i · K_b · m\n  3. Depression of Freezing Point: ΔT_f = i · K_f · m\n  4. Osmotic Pressure: π = i · C R T\n• Van't Hoff factor (i): i = 1 + (n - 1)α for dissociation; i = 1 + (1/n - 1)α for association.",
            keyFormulas = "• Raoult's Law: P_total = P_A° x_A + P_B° x_B\n• ΔT_b = i · K_b · m\n• ΔT_f = i · K_f · m\n• Osmotic Pressure: π = i CRT\n• Van't Hoff factor: i = Observed colligative property / Calculated property\n• Dissociation: α = (i - 1) / (n - 1)",
            totalQuestionsCount = 20,
            completedQuestionsCount = 17,
            masteryPercentage = 90,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "chem_ch2",
            subjectCode = "CHEM",
            chapterNumber = 2,
            title = "Electrochemistry",
            cbseWeightageMarks = 9,
            summaryNotes = "• Nernst Equation: E_cell = E°_cell - (0.0591 / n) log₁₀(Q) at 298 K.\n• Equilibrium Constant: log K_c = (n E°_cell) / 0.0591.\n• Gibbs Energy: ΔG° = -n F E°_cell.\n• Kohlrausch's Law of Independent Migration of Ions: Λ°_m(AxBy) = x λ°_m(A^y+) + y λ°_m(B^x-).\n• Degree of dissociation: α = Λ_m / Λ°_m, Dissociation constant: K_a = Cα² / (1 - α).",
            keyFormulas = "• E_cell = E°_cell - (0.0591/n) log [Products]/[Reactants]\n• ΔG° = -n F E°_cell\n• Molar conductivity: Λ_m = (κ × 1000) / Molarity\n• Kohlrausch's Law: Λ°_m = ν₊ λ₊° + ν₋ λ₋°\n• α = Λ_m / Λ°_m",
            totalQuestionsCount = 22,
            completedQuestionsCount = 11,
            masteryPercentage = 55,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "chem_ch3",
            subjectCode = "CHEM",
            chapterNumber = 3,
            title = "Aldehydes, Ketones & Carboxylic Acids",
            cbseWeightageMarks = 8,
            summaryNotes = "• Named Reactions & Mechanisms:\n  1. Rosenmund Reduction: Acyl chloride + H₂/Pd-BaSO₄ → Aldehyde.\n  2. Etard Reaction: Toluene + CrO₂Cl₂/CS₂ followed by H₃O⁺ → Benzaldehyde.\n  3. Aldol Condensation: Aldehydes/Ketones with α-hydrogen + dil. NaOH → β-hydroxy aldehyde.\n  4. Cannizzaro Reaction: Aldehydes without α-H (HCHO, PhCHO) + conc. KOH → Alcohol + Carboxylate salt (Disproportionation).\n  5. Clemmensen Reduction (Zn-Hg / conc. HCl) & Wolff-Kishner Reduction (NH₂NH₂ / KOH in ethylene glycol).\n  6. Tollens' & Fehling's Test: Tollens' gives silver mirror with all aldehydes.",
            keyFormulas = "• Tollens': R-CHO + 2[Ag(NH₃)₂]⁺ + 3OH⁻ → R-COO⁻ + 2Ag↓ + 4NH₃ + 2H₂O\n• Iodoform: R-COCH₃ + 3I₂ + 4NaOH → CHI₃↓ (yellow) + R-COONa + 3NaI + 3H₂O\n• HVZ Reaction: R-CH₂-COOH + X₂/Red P → R-CH(X)-COOH",
            totalQuestionsCount = 25,
            completedQuestionsCount = 12,
            masteryPercentage = 50,
            isOfflineReady = true
        ),

        // BIOLOGY
        ChapterEntity(
            id = "bio_ch1",
            subjectCode = "BIO",
            chapterNumber = 1,
            title = "Molecular Basis of Inheritance",
            cbseWeightageMarks = 10,
            summaryNotes = "• DNA Structure: Double helix (Watson & Crick), antiparallel 5'→3' and 3'→5', pitch = 3.4 nm, 10 bp per turn.\n• Central Dogma: DNA → (Transcription) → mRNA → (Translation) → Protein.\n• Semiconservative Replication: Meselson & Stahl experiment using ¹⁵N and ¹⁴N isotopes in E. coli.\n• Genetic Code: Triplet, degenerate, unambiguous, universal, AUG is initiator (codes for Methionine).\n• Lac Operon: Jacob & Monod model. Regulatory gene 'i' produces repressor. Inducer = Lactose. Structural genes: z (β-galactosidase), y (permease), a (transacetylase).",
            keyFormulas = "• Chargaff's Rule: [A] + [G] = [T] + [C] and [A]/[T] = [G]/[C] = 1 in dsDNA\n• Distance between base pairs = 0.34 nm = 0.34 × 10⁻⁹ m\n• Length of human DNA = 6.6 × 10⁹ bp × 0.34 × 10⁻⁹ m ≈ 2.2 m",
            totalQuestionsCount = 24,
            completedQuestionsCount = 18,
            masteryPercentage = 84,
            isOfflineReady = true
        ),
        ChapterEntity(
            id = "bio_ch2",
            subjectCode = "BIO",
            chapterNumber = 2,
            title = "Principles of Inheritance and Variation",
            cbseWeightageMarks = 9,
            summaryNotes = "• Mendel's Laws: Dominance, Segregation (Law of purity of gametes), Independent Assortment.\n• Incomplete Dominance (Snapdragon 1:2:1) & Codominance (ABO blood grouping - Iᴬ, Iᴮ, i alleles).\n• Chromosomal Theory of Inheritance (Sutton & Boveri). Morgan's work on Drosophila melanogaster.\n• Genetic Disorders:\n  - Mendelian (Haemophilia, Sickle-cell anaemia, Phenylketonuria, Thalassemia)\n  - Chromosomal (Down's Syndrome - Trisomy 21, Turner's Syndrome - 45 XO, Klinefelter's Syndrome - 47 XXY).",
            keyFormulas = "• Monohybrid phenotypic ratio: 3:1 | Genotypic ratio: 1:2:1\n• Dihybrid phenotypic ratio: 9:3:3:1\n• Recombination Frequency = (Total recombinants / Total progeny) × 100",
            totalQuestionsCount = 20,
            completedQuestionsCount = 15,
            masteryPercentage = 78,
            isOfflineReady = true
        )
    )

    val flashcards: List<FlashcardEntity> = listOf(
        // Math Flashcards
        FlashcardEntity(
            chapterId = "math_ch1",
            subjectCode = "MATH",
            frontTitle = "Equivalence Relation Definition",
            frontContent = "What 3 conditions must a relation R on set A satisfy to be an Equivalence Relation?",
            backExplanation = "1. Reflexive: (a,a) ∈ R for all a ∈ A.\n2. Symmetric: (a,b) ∈ R implies (b,a) ∈ R.\n3. Transitive: (a,b) ∈ R and (b,c) ∈ R implies (a,c) ∈ R.",
            formulaOrKeyPoint = "Reflexive + Symmetric + Transitive = Equivalence",
            isMastered = true,
            reviewCount = 4
        ),
        FlashcardEntity(
            chapterId = "math_ch2",
            subjectCode = "MATH",
            frontTitle = "Principal Value Range of Inverse Trigonometry",
            frontContent = "State the principal value branch (range) for sin⁻¹(x) and cos⁻¹(x).",
            backExplanation = "• Range of sin⁻¹(x) = [-π/2, π/2]\n• Range of cos⁻¹(x) = [0, π]\n• Domain for both is [-1, 1].",
            formulaOrKeyPoint = "sin⁻¹: [-π/2, π/2], cos⁻¹: [0, π]",
            isMastered = true,
            reviewCount = 5
        ),
        FlashcardEntity(
            chapterId = "math_ch3",
            subjectCode = "MATH",
            frontTitle = "Determinant Property for Inverse",
            frontContent = "What is the formula for the inverse of an n×n matrix A, and when is it invertible?",
            backExplanation = "A⁻¹ = (1 / |A|) · adj(A)\nMatrix A is invertible if and only if |A| ≠ 0 (i.e. A is a non-singular matrix).",
            formulaOrKeyPoint = "A⁻¹ = (adj A)/|A| with |A| ≠ 0",
            isMastered = true,
            reviewCount = 3
        ),
        FlashcardEntity(
            chapterId = "math_ch4",
            subjectCode = "MATH",
            frontTitle = "King's Property of Definite Integrals",
            frontContent = "State the famous King's property formula for ∫[a,b] f(x) dx.",
            backExplanation = "∫[a, b] f(x) dx = ∫[a, b] f(a + b - x) dx\nSpecial case for [0, a]: ∫[0, a] f(x) dx = ∫[0, a] f(a - x) dx.",
            formulaOrKeyPoint = "∫[a,b] f(x)dx = ∫[a,b] f(a+b-x)dx",
            isMastered = false,
            reviewCount = 2
        ),
        FlashcardEntity(
            chapterId = "math_ch5",
            subjectCode = "MATH",
            frontTitle = "Shortest Distance Between Skew Lines",
            frontContent = "Write vector formula for shortest distance between lines r = a₁ + λb₁ and r = a₂ + μb₂.",
            backExplanation = "d = | (a₂ - a₁) · (b₁ × b₂) | / | b₁ × b₂ |\nIf distance d = 0, the lines intersect.",
            formulaOrKeyPoint = "d = |(a₂ - a₁) · (b₁ × b₂)| / |b₁ × b₂|",
            isMastered = true,
            reviewCount = 3
        ),

        // Physics Flashcards
        FlashcardEntity(
            chapterId = "phys_ch1",
            subjectCode = "PHYS",
            frontTitle = "Gauss's Law Statement & Equation",
            frontContent = "State Gauss's Law in electrostatics and write its mathematical form.",
            backExplanation = "The total electric flux passing through any closed Gaussian surface in vacuum is equal to (1/ε₀) times the net charge enclosed within the surface.\nΦ = ∮ E · dA = Q_enclosed / ε₀.",
            formulaOrKeyPoint = "Φ = ∮ E·dA = q_enc / ε₀",
            isMastered = true,
            reviewCount = 4
        ),
        FlashcardEntity(
            chapterId = "phys_ch2",
            subjectCode = "PHYS",
            frontTitle = "Drift Velocity & Electric Current Relation",
            frontContent = "How is current I related to drift velocity v_d, and what is the formula for v_d?",
            backExplanation = "• Current: I = n · A · e · v_d\n• Drift Velocity: v_d = (e · E · τ) / m\nWhere n = electron density, A = cross section, τ = relaxation time.",
            formulaOrKeyPoint = "I = n A e v_d  and  v_d = e E τ / m",
            isMastered = false,
            reviewCount = 2
        ),
        FlashcardEntity(
            chapterId = "phys_ch3",
            subjectCode = "PHYS",
            frontTitle = "Lens Maker's Formula",
            frontContent = "State the Lens Maker's formula for a thin lens with radii of curvature R₁ and R₂.",
            backExplanation = "1/f = (μ_lens/μ_medium - 1) [ 1/R₁ - 1/R₂ ]\nFor air medium (μ_medium = 1): 1/f = (μ - 1)[ 1/R₁ - 1/R₂ ].",
            formulaOrKeyPoint = "1/f = (μ - 1)(1/R₁ - 1/R₂)",
            isMastered = false,
            reviewCount = 3
        ),

        // Chemistry Flashcards
        FlashcardEntity(
            chapterId = "chem_ch1",
            subjectCode = "CHEM",
            frontTitle = "Van't Hoff Factor for Dissociation",
            frontContent = "Express the degree of dissociation (α) in terms of Van't Hoff factor (i) and number of ions (n).",
            backExplanation = "For dissociation of 1 molecule into n ions:\ni = 1 + (n - 1)α\nTherefore, α = (i - 1) / (n - 1).",
            formulaOrKeyPoint = "α = (i - 1) / (n - 1)",
            isMastered = true,
            reviewCount = 4
        ),
        FlashcardEntity(
            chapterId = "chem_ch2",
            subjectCode = "CHEM",
            frontTitle = "Nernst Equation at 298 K",
            frontContent = "Write the Nernst Equation for a general galvanic cell at standard 298 K.",
            backExplanation = "E_cell = E°_cell - (0.0591 / n) log₁₀( [Anode ions]^x / [Cathode ions]^y )\nAt equilibrium, E_cell = 0 ⇒ E°_cell = (0.0591 / n) log K_c.",
            formulaOrKeyPoint = "E_cell = E°_cell - (0.0591/n) log Q",
            isMastered = false,
            reviewCount = 2
        ),
        FlashcardEntity(
            chapterId = "chem_ch3",
            subjectCode = "CHEM",
            frontTitle = "Aldol Condensation vs Cannizzaro Reaction",
            frontContent = "What is the key structural requirement distinguishing Aldol Condensation from Cannizzaro reaction?",
            backExplanation = "• Aldol Condensation requires at least ONE α-hydrogen atom (e.g. CH₃CHO, CH₃COCH₃).\n• Cannizzaro reaction occurs ONLY in aldehydes WITHOUT α-hydrogen (e.g. HCHO, C₆H₅CHO) with 50% KOH.",
            formulaOrKeyPoint = "Aldol = α-H present | Cannizzaro = No α-H",
            isMastered = false,
            reviewCount = 3
        ),

        // Biology Flashcards
        FlashcardEntity(
            chapterId = "bio_ch1",
            subjectCode = "BIO",
            frontTitle = "Lac Operon Structural Genes",
            frontContent = "Name the 3 structural genes of the lac operon and their enzyme products.",
            backExplanation = "1. 'z' gene: codes for β-galactosidase (hydrolyzes lactose into glucose & galactose)\n2. 'y' gene: codes for Permease (increases membrane permeability to β-galactosides)\n3. 'a' gene: codes for Transacetylase.",
            formulaOrKeyPoint = "z → β-galactosidase, y → Permease, a → Transacetylase",
            isMastered = true,
            reviewCount = 5
        )
    )

    val sampleQuestions: Map<String, List<PracticeQuestion>> = mapOf(
        "math_ch1" to listOf(
            PracticeQuestion(
                questionText = "Let R be a relation on the set N of natural numbers defined by R = {(a, b) : a = b - 2, b > 6}. Which of the following is correct?",
                options = listOf("(2, 4) ∈ R", "(3, 8) ∈ R", "(6, 8) ∈ R", "(8, 7) ∈ R"),
                correctOptionIndex = 2,
                explanation = "For b > 6, let b = 8. Then a = 8 - 2 = 6. Thus, (6, 8) ∈ R since 8 > 6 and 6 = 8 - 2.",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2024"
            ),
            PracticeQuestion(
                questionText = "Assertion (A): The function f: R → R given by f(x) = x³ is a bijective function.\nReason (R): Every strictly increasing function on R is both injective and surjective.",
                options = listOf(
                    "Both (A) and (R) are true and (R) is the correct explanation of (A)",
                    "Both (A) and (R) are true but (R) is NOT the correct explanation of (A)",
                    "(A) is true but (R) is false",
                    "(A) is false but (R) is true"
                ),
                correctOptionIndex = 0,
                explanation = "f'(x) = 3x² ≥ 0 for all x ∈ R, so f(x) is strictly increasing, hence injective. As x → ±∞, f(x) → ±∞, range is ℝ, so onto. Bijective!",
                questionType = QuestionType.ASSERTION_REASON,
                pyqYear = "CBSE 2023"
            ),
            PracticeQuestion(
                questionText = "If A = {1, 2, 3}, then number of equivalence relations containing (1, 2) is:",
                options = listOf("1", "2", "3", "4"),
                correctOptionIndex = 1,
                explanation = "The smallest equivalence relation is R₁ = {(1,1), (2,2), (3,3), (1,2), (2,1)}. The only larger one is the universal relation R₂ = A × A. Thus, total is 2.",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2022"
            )
        ),
        "phys_ch1" to listOf(
            PracticeQuestion(
                questionText = "An electric dipole of moment p is placed in a uniform electric field E. The torque acting on the dipole is:",
                options = listOf("p · E", "p × E", "p / E", "Zero"),
                correctOptionIndex = 1,
                explanation = "Torque on dipole in electric field is given by vector cross product τ = p × E with magnitude |p||E| sinθ.",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2024"
            ),
            PracticeQuestion(
                questionText = "Assertion (A): Electric field lines never cross each other.\nReason (R): If they cross, at the point of intersection there will be two directions of electric field, which is impossible.",
                options = listOf(
                    "Both (A) and (R) are true and (R) is the correct explanation of (A)",
                    "Both (A) and (R) are true but (R) is NOT the correct explanation of (A)",
                    "(A) is true but (R) is false",
                    "(A) is false but (R) is true"
                ),
                correctOptionIndex = 0,
                explanation = "The tangent to a field line gives field direction. Two tangents at a single point would mean two unique net field directions, which is physically impossible.",
                questionType = QuestionType.ASSERTION_REASON,
                pyqYear = "CBSE 2024"
            )
        ),
        "chem_ch1" to listOf(
            PracticeQuestion(
                questionText = "Which of the following aqueous solutions will have the highest boiling point?",
                options = listOf("1.0 M Glucose", "1.0 M NaCl", "1.0 M CaCl₂", "1.0 M AlCl₃"),
                correctOptionIndex = 3,
                explanation = "ΔT_b = i · K_b · m. AlCl₃ dissociates into 4 ions (Al³⁺ + 3Cl⁻), so i = 4 (highest among the choices), giving highest boiling point elevation.",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2024"
            ),
            PracticeQuestion(
                questionText = "A mixture of ethanol and acetone shows positive deviation from Raoult's law because:",
                options = listOf(
                    "Ethanol-acetone attractive forces are stronger than pure components",
                    "Acetone gets in between ethanol molecules and weakens hydrogen bonding",
                    "Vapour pressure decreases upon mixing",
                    "ΔH_mixing is negative"
                ),
                correctOptionIndex = 1,
                explanation = "In pure ethanol, molecules are held by strong intermolecular H-bonds. On adding acetone, its molecules get between ethanol molecules and break some H-bonds, increasing escape tendency (positive deviation).",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2023"
            )
        ),
        "bio_ch1" to listOf(
            PracticeQuestion(
                questionText = "In a dsDNA sample, if Adenine constitutes 30% of bases, what will be the percentage of Cytosine?",
                options = listOf("30%", "20%", "40%", "70%"),
                correctOptionIndex = 1,
                explanation = "According to Chargaff's rule, A = T = 30%. Therefore, A + T = 60%. Remaining G + C = 40%, and since G = C, Cytosine = 40% / 2 = 20%.",
                questionType = QuestionType.MCQ,
                pyqYear = "CBSE 2024"
            )
        )
    )

    val peerDiscussions: List<PeerDiscussionEntity> = listOf(
        PeerDiscussionEntity(
            groupName = "Math 100/100 Board Club",
            subjectCode = "MATH",
            authorName = "Aarav Sharma",
            authorAvatarBadge = "AS",
            title = "Shortcut trick for King's property in Definite Integrals?",
            questionOrNote = "Whenever we see ∫[0, π/2] (sin^n x) / (sin^n x + cos^n x) dx, the answer is always (b-a)/2 = π/4! Can someone confirm if CBSE steps allow writing King's rule directly or must we show substitution step by step?",
            upvotes = 34,
            repliesCount = 8,
            isSolved = true,
            timestamp = System.currentTimeMillis() - 3600000 * 5
        ),
        PeerDiscussionEntity(
            groupName = "Physics Derivation Warriors",
            subjectCode = "PHYS",
            authorName = "Diya Patel",
            authorAvatarBadge = "DP",
            title = "Lens Maker's formula sign convention doubt",
            questionOrNote = "When taking convex lens, R1 is positive (+R1) and R2 is negative (-R2), so 1/f = (μ-1)(1/R1 + 1/R2). Don't forget that R2 center of curvature lies on the left side!",
            upvotes = 42,
            repliesCount = 12,
            isSolved = true,
            timestamp = System.currentTimeMillis() - 3600000 * 12
        ),
        PeerDiscussionEntity(
            groupName = "Organic Chemistry Reactions Circle",
            subjectCode = "CHEM",
            authorName = "Rohan Verma",
            authorAvatarBadge = "RV",
            title = "Distinguish between Pentan-2-one and Pentan-3-one",
            questionOrNote = "Iodoform test! Pentan-2-one has CH3-C=O group, so gives yellow precipitate of CHI3 with NaOH + I2. Pentan-3-one doesn't react. Very high yield 2-mark CBSE question!",
            upvotes = 56,
            repliesCount = 15,
            isSolved = true,
            timestamp = System.currentTimeMillis() - 3600000 * 20
        ),
        PeerDiscussionEntity(
            groupName = "Biology Diagrams & Genetics",
            subjectCode = "BIO",
            authorName = "Sneha Nair",
            authorAvatarBadge = "SN",
            title = "Meselson & Stahl experiment density gradient medium",
            questionOrNote = "Remember to mention CsCl (Cesium Chloride) density gradient centrifugation and 15NH4Cl heavy isotope (not radioactive!) for 3 full marks in Central Dogma replication questions.",
            upvotes = 29,
            repliesCount = 6,
            isSolved = true,
            timestamp = System.currentTimeMillis() - 3600000 * 30
        )
    )
}
