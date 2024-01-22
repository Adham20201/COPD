import pandas as pd
from sklearn import model_selection
from sklearn.discriminant_analysis import QuadraticDiscriminantAnalysis

dataset = pd.read_csv('https://raw.githubusercontent.com/omarzyasser/COPD19304/main/COPDpatients.csv')

array = dataset.values
x = dataset.drop('COPDSEVERITY', axis=1)
y = dataset['COPDSEVERITY']
X_train, X_validation, Y_train, Y_validation = model_selection.train_test_split(x, y, test_size=0.2, random_state=7)
QDA = QuadraticDiscriminantAnalysis()
QDA.fit(X_train, Y_train)
predictions = QDA.predict(X_validation)

def main(age, mwt1, packhistory, fev1, fvc, smkoing, gender):

    new_data = {
        'AGE': age,
        'MWT1': mwt1,
        'PackHistory': packhistory,
        'FEV1': fev1,
        'FVC': fvc,
        'smoking': smkoing,  # Assuming 1 for non-smoking and 2 for smoking
        'gender': gender  # Assuming 0 for female
    }

    new_df = pd.DataFrame([new_data])
    predictions = QDA.predict(new_df)

    return str(predictions[0])
